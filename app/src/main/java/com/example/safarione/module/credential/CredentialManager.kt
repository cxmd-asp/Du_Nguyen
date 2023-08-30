package com.example.safarione.module.credential

import com.example.safarione.model.auth.AuthInfo
import kotlinx.coroutines.flow.Flow

interface CredentialManager {

    val credential: Flow<AuthInfo?>

    suspend fun authenticate(authInfo: AuthInfo)

    suspend fun clearCredential()
}