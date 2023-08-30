package com.example.safarione.injection

import com.example.safarione.module.credential.CredentialManager
import com.example.safarione.module.database.ChatDatabase
import com.example.safarione.repository.AuthRepository
import com.example.safarione.repository.ChatMessageRepository
import com.example.safarione.repository.ChatRoomRepository
import com.example.safarione.repository.MemberRepository
import com.example.safarione.repository.impl.AuthRepositoryImpl
import com.example.safarione.repository.impl.ChatMessageRepositoryImpl
import com.example.safarione.repository.impl.ChatRoomRepositoryImpl
import com.example.safarione.repository.impl.MemberRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        database: ChatDatabase,
        credentialManager: CredentialManager
    ): ChatMessageRepository = ChatMessageRepositoryImpl(
        credentialManager = credentialManager,
        roomDataSource = database.chatRoomDao(),
        messageDataSource = database.messageDao()
    )

    @Provides
    @Singleton
    fun provideAuthRepository(
        database: ChatDatabase,
        credentialManager: CredentialManager
    ): AuthRepository = AuthRepositoryImpl(
        credentialManager = credentialManager,
        userDataSource = database.userDao()
    )

    @Provides
    @Singleton
    fun provideMemberRepository(database: ChatDatabase): MemberRepository = MemberRepositoryImpl(
        memberDataSource = database.userDao()
    )

    @Provides
    @Singleton
    fun provideChatRoomRepository(
        database: ChatDatabase,
        credentialManager: CredentialManager
    ): ChatRoomRepository = ChatRoomRepositoryImpl(
        credentialManager = credentialManager,
        chatRoomDataSource = database.chatRoomDao(),
        chatMemberDataSource = database.chatRoomMemberDao()
    )
}