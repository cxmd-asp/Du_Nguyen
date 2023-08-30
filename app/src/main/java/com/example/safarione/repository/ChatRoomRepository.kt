package com.example.safarione.repository

import com.example.safarione.model.chat.room.ChatRoom
import com.example.safarione.model.user.User
import kotlinx.coroutines.flow.Flow

interface ChatRoomRepository {

    fun getChatRooms() : Flow<List<ChatRoom>>

    suspend fun getRoom(roomId: Long) : ChatRoom?

    suspend fun createSingleConversationIfNotExist(member: User) : ChatRoom

    suspend fun createConversation(title: String, members: List<User>): ChatRoom
}