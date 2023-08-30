package com.example.safarione.ui.splash.contract

import kotlinx.coroutines.flow.Flow

interface SplashContract {

    interface ViewModel {

        val eventOfAuthenticationState: Flow<Boolean>

        fun checkAuthenticationState()
    }
}