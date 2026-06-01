package com.example.data.repository

import com.example.network.dto.CreateSessionRequest
import com.example.network.dto.LoginRequest
import com.example.domain.repository.AuthRepository
import com.example.network.network.TmdbApi.TmdbApi

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