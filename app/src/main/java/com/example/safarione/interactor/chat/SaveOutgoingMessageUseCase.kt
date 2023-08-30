package com.example.safarione.interactor.chat

import com.example.safarione.interactor.CoroutineUseCase
import com.example.safarione.model.chat.message.Message
import com.example.safarione.model.status.MessageStatus
import com.example.safarione.module.credential.CredentialManager
import com.example.safarione.repository.ChatMessageRepository
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@ServiceScoped
class SaveOutgoingMessageUseCase @Inject constructor(
    private val credentialManager: CredentialManager,
    private val chatMessageRepository: ChatMessageRepository
) : CoroutineUseCase<Unit, OutgoingMessage>() {

    override suspend fun run(params: OutgoingMessage) {
        val authInfo = requireNotNull(credentialManager.credential.first())
        chatMessageRepository.saveIncomingMessage(params.roomId, Message(
            senderId = authInfo.uuid,
            message = params.content,
            status = MessageStatus.RECEIVED
        ))
    }
}

data class OutgoingMessage(
    val roomId: Long,
    val content: String
)