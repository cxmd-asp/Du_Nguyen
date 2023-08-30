package com.example.safarione.interactor.chat

import androidx.paging.PagingData
import com.example.safarione.interactor.FlowUseCase
import com.example.safarione.model.chat.message.Message
import com.example.safarione.repository.ChatMessageRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetPagingRoomMessageUseCase @Inject constructor(
    private val messageRepository: ChatMessageRepository
) : FlowUseCase<PagingData<Message>, Long> {

    override fun create(params: Long): Flow<PagingData<Message>> = messageRepository.loadPagedMessageInRoom(params)

}