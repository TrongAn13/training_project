package com.example.domain.usecase

import com.example.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): String {
        return authRepository.login(username, password)
    }
}
