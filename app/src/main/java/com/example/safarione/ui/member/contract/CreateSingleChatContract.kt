package com.example.safarione.ui.member.contract

import androidx.paging.PagingData
import com.example.safarione.model.chat.room.ChatRoom
import com.example.safarione.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface CreateSingleChatContract {

    interface ViewModel {

        val searchQuery: MutableStateFlow<String>

        val eventOfSingleChatCreated : Flow<ChatRoom?>

        val members: Flow<PagingData<User>>

        fun requestCreateChat(user: User)
    }
}