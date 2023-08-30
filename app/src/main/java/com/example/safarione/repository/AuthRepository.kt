package com.example.safarione.repository

import com.example.safarione.model.auth.AuthInfo

interface AuthRepository {

    suspend fun logIn(username: String, password: String) : AuthInfo

    suspend fun logOut()
}