package com.example.safarione.ui.member.contract

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.safarione.interactor.chat.CreateSingleRoomChatUseCase
import com.example.safarione.interactor.member.GetMemberFlowUseCase
import com.example.safarione.model.chat.room.ChatRoom
import com.example.safarione.model.user.User
import com.example.safarione.utils.eventChannelOf
import com.example.safarione.utils.useCaseContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class CreateSingleChatViewModel @Inject constructor(
    application: Application,
    private val getAllMemberFlowUseCase: GetMemberFlowUseCase,
    private val createSingleRoomChatUseCase: CreateSingleRoomChatUseCase
) : AndroidViewModel(application), CreateSingleChatContract.ViewModel {

    private val eventChannelOfRoomCreated = eventChannelOf<ChatRoom?>()

    override val searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val stateOfAllMembers: Flow<PagingData<User>> = searchQuery
        .debounce(500L)
        .flatMapLatest {
            getAllMemberFlowUseCase.create(it)
        }.cachedIn(viewModelScope)

    override val eventOfSingleChatCreated: Flow<ChatRoom?> =
        eventChannelOfRoomCreated.flow.shareIn(viewModelScope, SharingStarted.Eagerly)

    override val members: Flow<PagingData<User>>
        get() = stateOfAllMembers

    override fun requestCreateChat(user: User) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            val realCause = (throwable as? RuntimeException)?.cause ?: throwable
            if (realCause is CancellationException) return@CoroutineExceptionHandler
            else if (realCause is Exception) {
                Timber.e(realCause)
                viewModelScope.launch {
                    eventChannelOfRoomCreated.send(null)
                }
            }
        }) {
            createSingleRoomChatUseCase.fetch(useCaseContext, user).let {
                eventChannelOfRoomCreated.send(it)
            }
        }
    }
}