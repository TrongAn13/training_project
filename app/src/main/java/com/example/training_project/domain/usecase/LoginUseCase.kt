package com.example.training_project.domain.usecase

import com.example.training_project.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): String {
        return authRepository.login(username, password)
    }
}
