package com.example.safarione.repository.impl

import com.example.safarione.model.auth.AuthInfo
import com.example.safarione.module.credential.CredentialManager
import com.example.safarione.module.database.dao.UserDao
import com.example.safarione.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val credentialManager: CredentialManager,
    private val userDataSource: UserDao
) : AuthRepository {

    override suspend fun logIn(username: String, password: String): AuthInfo {
        require(username.isNotEmpty())
        require(password.isNotEmpty())
        val user = userDataSource.getByPhone(username)
        require(user != null) {
            "User not found"
        }
        val authInfo = AuthInfo(user.id, username, password)
        credentialManager.authenticate(
            authInfo
        )
        return authInfo
    }

    override suspend fun logOut() {
        credentialManager.clearCredential()
    }
}