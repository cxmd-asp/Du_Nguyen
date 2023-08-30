package com.example.safarione.interactor.chat

import com.example.safarione.interactor.CoroutineUseCase
import com.example.safarione.model.chat.room.ChatRoom
import com.example.safarione.repository.ChatRoomRepository
import javax.inject.Inject

class GetChatRoomUseCase @Inject constructor(
    private val roomRepository: ChatRoomRepository
) : CoroutineUseCase<ChatRoom, Long>() {

    override suspend fun run(params: Long): ChatRoom =
        requireNotNull(roomRepository.getRoom(params))
}