package com.example.safarione.module.credential.impl

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.safarione.model.auth.AuthInfo
import com.example.safarione.module.credential.CredentialManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("credential")

class CredentialManagerImpl(
    @ApplicationContext context: Context
) : CredentialManager {

    private val dataStore = context.dataStore

    companion object {
        private val PREF_USER_ID = longPreferencesKey("uid")
        private val PREF_USERNAME = stringPreferencesKey("username")
        private val PREF_PASSWORD = stringPreferencesKey("password")
    }

    override val credential: Flow<AuthInfo?> =
        dataStore.data.map {
            val uid = it[PREF_USER_ID] ?: return@map null
            val userName = it[PREF_USERNAME] ?: return@map null
            val password = it[PREF_PASSWORD] ?: return@map null
            if (uid <= 0 || userName.isEmpty() || password.isEmpty()) return@map null
            AuthInfo(uid, userName, password)
        }

    override suspend fun authenticate(authInfo: AuthInfo) {
        dataStore.edit {
            it[PREF_USER_ID] = authInfo.uuid
            it[PREF_USERNAME] = authInfo.username
            it[PREF_PASSWORD] = authInfo.password
        }
    }

    override suspend fun clearCredential() {
        dataStore.edit {
            it[PREF_USER_ID] = 0L
            it[PREF_USERNAME] = ""
            it[PREF_PASSWORD] = ""
        }
    }
}