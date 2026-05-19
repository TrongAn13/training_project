package com.example.training_project.domain.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): String
}