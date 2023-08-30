package com.example.safarione.interactor.auth

import com.example.safarione.interactor.CoroutineUseCase
import com.example.safarione.model.auth.AuthInfo
import com.example.safarione.repository.AuthRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LoginUseCase @Inject constructor(private val authRepository: AuthRepository):
    CoroutineUseCase<AuthInfo, LoginRequest>() {

    override suspend fun run(params: LoginRequest): AuthInfo {
        return authRepository.logIn(params.username, params.password)
    }
}

data class LoginRequest(
    val username: String,
    val password: String
)