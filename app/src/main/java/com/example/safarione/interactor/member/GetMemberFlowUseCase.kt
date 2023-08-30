package com.example.safarione.interactor.member

import androidx.paging.PagingData
import com.example.safarione.interactor.FlowUseCase
import com.example.safarione.model.user.User
import com.example.safarione.repository.MemberRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetMemberFlowUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) : FlowUseCase<PagingData<User>, String?>{

    override fun create(params: String?): Flow<PagingData<User>> =
        memberRepository.getPagingMembers(params)
}