package com.example.safarione.injection

import android.content.Context
import com.example.safarione.module.database.ChatDatabase
import com.example.safarione.module.database.createChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataSourceModule {

    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext context: Context) : ChatDatabase =
        createChatDatabase(context)
}