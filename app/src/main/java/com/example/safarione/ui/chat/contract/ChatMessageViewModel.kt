package com.example.safarione.ui.chat.contract

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.safarione.interactor.chat.GetChatRoomUseCase
import com.example.safarione.interactor.chat.GetPagingRoomMessageUseCase
import com.example.safarione.model.chat.message.Message
import com.example.safarione.model.chat.room.ChatRoom
import com.example.safarione.utils.useCaseContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChatMessageViewModel @Inject constructor(
    application: Application,
    private val getPagingRoomMessageUseCase: GetPagingRoomMessageUseCase,
    private val getChatRoomUseCase: GetChatRoomUseCase
) : AndroidViewModel(application), ChatMessageContract.ViewModel {

    private val stateOfRoomId = MutableStateFlow(0L)

    override val room: StateFlow<ChatRoom?> = stateOfRoomId.map {
        if (it <= 0L) null
        else getChatRoomUseCase.fetch(useCaseContext, it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(600), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val conversation: Flow<PagingData<Message>> =
        stateOfRoomId.flatMapLatest {
            if (it <= 0L) flowOf(PagingData.empty())
            else getPagingRoomMessageUseCase.create(it).cachedIn(viewModelScope)
        }

    override val message: MutableStateFlow<String?> = MutableStateFlow("")

    override val enableSend: StateFlow<Boolean> = message.map {
        !it.isNullOrEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(600), false)

    override fun setRoomId(roomId: Long) {
        stateOfRoomId.value = roomId
    }

}