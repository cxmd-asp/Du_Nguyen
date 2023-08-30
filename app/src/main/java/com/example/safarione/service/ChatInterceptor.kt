package com.example.safarione.service

import kotlinx.coroutines.flow.StateFlow

interface ChatInterceptor {

    val isConnected : StateFlow<ConnectionStatus>

    fun sendToUser(toId: Long, message: String)

    fun sendToRoom(roomId: Long, message: String)
}

enum class ConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED
}