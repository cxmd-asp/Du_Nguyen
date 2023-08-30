package com.example.safarione.repository.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.safarione.model.user.User
import com.example.safarione.model.user.toModel
import com.example.safarione.module.database.dao.UserDao
import com.example.safarione.module.database.entity.UserEntity
import com.example.safarione.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberDataSource: UserDao
) : MemberRepository {

    override fun findMember(userId: Long): User? = memberDataSource.getById(userId)?.toModel()

    override fun getAllMembers(): Flow<List<User>> = memberDataSource.getAll().map {
        it.map(UserEntity::toModel)
    }

    override fun getPagingMembers(keyword: String?): Flow<PagingData<User>> = Pager(
        PagingConfig(
            pageSize = 10
        )
    ) {
        if (keyword.isNullOrEmpty()) memberDataSource.getPaging()
        else memberDataSource.getPaging(keyword)
    }.flow.map {
        it.map(UserEntity::toModel)
    }
}