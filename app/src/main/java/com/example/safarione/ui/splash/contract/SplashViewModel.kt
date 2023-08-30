package com.example.safarione.ui.splash.contract

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.safarione.module.credential.CredentialManager
import com.example.safarione.utils.eventChannelOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application,
    private val credentialManager: CredentialManager
) : AndroidViewModel(application), SplashContract.ViewModel {

    private val eventChannelOfAuthenticationState = eventChannelOf<Boolean>()

    override val eventOfAuthenticationState: Flow<Boolean> = eventChannelOfAuthenticationState.flow.shareIn(viewModelScope, SharingStarted.Eagerly)

    override fun checkAuthenticationState() {
        viewModelScope.launch {
            val auth = credentialManager.credential.first() != null
            eventChannelOfAuthenticationState.send(auth)
        }
    }
}