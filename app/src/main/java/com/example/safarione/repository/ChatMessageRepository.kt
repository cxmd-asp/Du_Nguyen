package com.example.safarione.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.safarione.model.chat.message.Message
import com.example.safarione.module.database.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatMessageRepository {

    fun loadPagedMessageInRoom(roomId: Long) : Flow<PagingData<Message>>

    suspend fun saveIncomingMessage(roomId: Long, content: Message)

    suspend fun saveOutgoingMessage(roomId: Long, content: Message)
}