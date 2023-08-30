package com.example.safarione.repository

import androidx.paging.PagingData
import com.example.safarione.model.user.User
import kotlinx.coroutines.flow.Flow

interface MemberRepository {

    fun findMember(userId: Long): User?

    fun getAllMembers() : Flow<List<User>>

    fun getPagingMembers(keyword: String?) : Flow<PagingData<User>>
}