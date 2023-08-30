package com.example.safarione.ui.auth.login.contract

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.safarione.interactor.auth.LoginRequest
import com.example.safarione.interactor.auth.LoginUseCase
import com.example.safarione.utils.eventChannelOf
import com.example.safarione.utils.useCaseContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val loginUseCase: LoginUseCase
) : AndroidViewModel(application), LoginContract.ViewModel {

    private val eventOfLoginProcess = eventChannelOf<LoginContract.LoginResult>()

    private val hasOngoingTask = MutableStateFlow(false)

    override val username: MutableStateFlow<String?> = MutableStateFlow(null)

    override val password: MutableStateFlow<String?> = MutableStateFlow(null)

    override val enableLogin: StateFlow<Boolean> =
        combine(username, password, hasOngoingTask) { username, password, busy ->
            !busy && !username.isNullOrEmpty() && !password.isNullOrEmpty()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), false)

    override val resultOfLoginProcess: Flow<LoginContract.LoginResult> =
        eventOfLoginProcess.flow.shareIn(viewModelScope, SharingStarted.Eagerly)

    override fun onLogin() {
        if (hasOngoingTask.value) {
            return
        }
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            val realCause = (throwable as? RuntimeException)?.cause ?: throwable
            if (realCause is CancellationException) return@CoroutineExceptionHandler
            else if (realCause is Exception) {
                viewModelScope.launch {
                    eventOfLoginProcess.send(
                        LoginContract.LoginResult.Failed(
                            realCause.message ?: "Unknown error"
                        )
                    )
                }
            }
        }) {
            hasOngoingTask.value = true
            val username = username.value
            val password = password.value
            require(!username.isNullOrEmpty()) {
                "Username cannot be empty"
            }
            require(!password.isNullOrEmpty()) {
                "Password cannot be empty"
            }
            loginUseCase.fetch(useCaseContext, LoginRequest(username, password))
            eventOfLoginProcess.send(LoginContract.LoginResult.Success)
        }.invokeOnCompletion {
            hasOngoingTask.value = false
        }
    }
}