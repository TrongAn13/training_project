package com.example.domain.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): String
}