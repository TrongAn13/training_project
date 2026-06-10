package com.example.training_project.data.repository

import com.example.training_project.data.network.TmdbApi
import com.example.training_project.data.remote.DTO.CreateSessionRequest
import com.example.training_project.data.remote.DTO.LoginRequest
import com.example.training_project.domain.repository.AuthRepository

class AuthRepositoryImpl(private val apiService: TmdbApi) : AuthRepository {
    override suspend fun login(username: String, password: String): String {
        val tokenResponse = apiService.createRequestToken()

        val validatedToken = apiService.validateWithLogin(
            LoginRequest(
                username = username,
                password = password,
                requestToken = tokenResponse.requestToken
            )
        )
        val sessionResponse = apiService.createSession(
            CreateSessionRequest(
                requestToken = validatedToken.requestToken
            )
        )
        return sessionResponse.sessionId
    }
}