package com.example.safarione.interactor.chat

import com.example.safarione.interactor.CoroutineUseCase
import com.example.safarione.model.chat.room.ChatRoom
import com.example.safarione.model.user.User
import com.example.safarione.module.credential.CredentialManager
import com.example.safarione.repository.ChatRoomRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateSingleRoomChatUseCase @Inject constructor(
    private val credentialManager: CredentialManager,
    private val roomRepository: ChatRoomRepository
) : CoroutineUseCase<ChatRoom, User>() {

    override suspend fun run(params: User): ChatRoom {
        val userId = credentialManager.credential.first()
        requireNotNull(userId)
        if (userId.uuid == params.id) throw IllegalStateException("Sorry, you cannot chat with yourself")
        return roomRepository.createSingleConversationIfNotExist(params)
    }
}