package com.example.safarione.interactor.chat

import com.example.safarione.interactor.CoroutineUseCase
import com.example.safarione.model.chat.message.Message
import com.example.safarione.repository.ChatMessageRepository
import com.example.safarione.repository.ChatRoomRepository
import com.example.safarione.repository.MemberRepository
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class SaveIncomingMessageUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val roomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository
) : CoroutineUseCase<Unit, IncomingMessage>() {

    override suspend fun run(params: IncomingMessage) {
        val user = memberRepository.findMember(params.roomId)
        requireNotNull(user)
        val room = roomRepository.createSingleConversationIfNotExist(user)
        chatMessageRepository.saveIncomingMessage(room.id, params.content)
    }
}

data class IncomingMessage(
    val roomId: Long,
    val content: Message
)