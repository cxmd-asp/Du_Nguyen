package com.example.safarione.ui.auth.login.contract

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface LoginContract {

    interface ViewModel {

        val username: MutableStateFlow<String?>

        val password: MutableStateFlow<String?>

        val enableLogin: StateFlow<Boolean>

        val resultOfLoginProcess: Flow<LoginResult>

        fun onLogin()
    }

    sealed interface LoginResult {

        object Success : LoginResult

        data class Failed(val message: String) : LoginResult
    }
}