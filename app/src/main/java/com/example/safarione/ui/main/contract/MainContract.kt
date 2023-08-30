package com.example.safarione.ui.main.contract

import com.example.safarione.service.ChatInterceptor
import kotlinx.coroutines.flow.StateFlow

interface MainContract {

    interface ChatInterceptorDelegate {

        fun getChatInterceptor() : StateFlow<ChatInterceptor?>
    }

    interface ViewModel {

        fun logOut()
    }
}