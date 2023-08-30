package com.example.safarione.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.safarione.model.chat.message.Message
import com.example.safarione.model.status.MessageStatus
import com.example.safarione.module.credential.CredentialManager
import com.example.safarione.module.database.dao.ChatRoomDao
import com.example.safarione.module.database.dao.MessageDao
import com.example.safarione.module.database.entity.MessageEntity
import com.example.safarione.repository.ChatMessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class ChatMessageRepositoryImpl @Inject constructor(
    private val credentialManager: CredentialManager,
    private val roomDataSource: ChatRoomDao,
    private val messageDataSource: MessageDao
) : ChatMessageRepository {

    override fun loadPagedMessageInRoom(roomId: Long): Flow<PagingData<Message>> =
        Pager(
            PagingConfig(
                pageSize = 10
            )
        ) {
            messageDataSource.loadPagedMessageInRoom(roomId)
        }.flow.map {
            val uuid = credentialManager.credential.first()?.uuid
            it.map { entity ->
                Message(
                    uuid = entity.id,
                    senderId = entity.senderId,
                    status = entity.status,
                    message = entity.message,
                    date = Date(entity.date),
                    isMyMessage = uuid == entity.senderId
                )
            }
        }

    override suspend fun saveIncomingMessage(roomId: Long, content: Message) {
        messageDataSource.insertAll(
            MessageEntity(
                date = content.date.time,
                senderId = content.senderId,
                chatRoomId = roomId,
                message = content.message,
                status = MessageStatus.RECEIVED
            )
        )
    }

    override suspend fun saveOutgoingMessage(roomId: Long, content: Message) {
        messageDataSource.insertAll(
            MessageEntity(
                date = content.date.time,
                senderId = content.senderId,
                chatRoomId = roomId,
                message = content.message,
                status = MessageStatus.RECEIVED
            )
        )
    }
}