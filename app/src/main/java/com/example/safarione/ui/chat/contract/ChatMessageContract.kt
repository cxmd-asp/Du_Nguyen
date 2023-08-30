package com.example.safarione.ui.chat.contract

import androidx.paging.PagingData
import com.example.safarione.model.chat.message.Message
import com.example.safarione.model.chat.room.ChatRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ChatMessageContract {

    interface ViewModel {

        val room : StateFlow<ChatRoom?>

        val conversation: Flow<PagingData<Message>>

        val message: MutableStateFlow<String?>

        val enableSend: StateFlow<Boolean>

        fun setRoomId(roomId: Long)
    }
}