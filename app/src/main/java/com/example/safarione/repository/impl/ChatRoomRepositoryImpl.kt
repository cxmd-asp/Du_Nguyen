package com.example.safarione.repository.impl

import com.example.safarione.model.chat.room.ChatRoom
import com.example.safarione.model.chat.room.toModel
import com.example.safarione.model.user.User
import com.example.safarione.module.credential.CredentialManager
import com.example.safarione.module.database.dao.ChatRoomDao
import com.example.safarione.module.database.dao.ChatRoomMemberDao
import com.example.safarione.module.database.entity.ChatRoomEntity
import com.example.safarione.module.database.entity.RefChatMemberEntity
import com.example.safarione.repository.ChatRoomRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val credentialManager: CredentialManager,
    private val chatRoomDataSource : ChatRoomDao,
    private val chatMemberDataSource: ChatRoomMemberDao
) : ChatRoomRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getChatRooms(): Flow<List<ChatRoom>> =
        credentialManager.credential.flatMapLatest { info ->
            info ?: return@flatMapLatest emptyFlow()
            chatRoomDataSource.loadByOwnerId(info.uuid).map {
                it.map { entity ->
                    entity.toModel(emptyList())
                }
            }
        }

    override suspend fun getRoom(roomId: Long): ChatRoom? =
        chatRoomDataSource.findOneById(roomId)?.toModel(emptyList())

    override suspend fun createSingleConversationIfNotExist(member: User): ChatRoom {
        val userId = credentialManager.credential.first()?.uuid
        require(userId != null && userId > 0L)
        val existRoom = chatRoomDataSource.findOneById(member.id)
        if (existRoom != null) return existRoom.toModel(members = listOf(member))
        val room = ChatRoomEntity(
                id = member.id,
                title = member.phone,
                ownerId = userId
        )
        val roomId = chatRoomDataSource.createChatRoom(room)
        chatMemberDataSource.insertAll(RefChatMemberEntity(userId = member.id, chatRoomId = roomId))
        return requireNotNull(chatRoomDataSource.findOneById(roomId)).toModel(listOf(member))
    }

    override suspend fun createConversation(title: String, members: List<User>): ChatRoom {
        val userId = credentialManager.credential.first()?.uuid
        require(userId != null && userId > 0L)
        val room = ChatRoomEntity(
            title = title,
            ownerId = userId,
        )
        val roomId = chatRoomDataSource.createChatRoom(room)
        members.map {
            RefChatMemberEntity(userId = it.id, chatRoomId = roomId)
        }.toTypedArray().let {
            chatMemberDataSource.insertAll(*it)
        }
        return requireNotNull(chatRoomDataSource.findOneById(roomId)).toModel(members)
    }
}