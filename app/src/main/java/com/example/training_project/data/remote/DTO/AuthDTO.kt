package com.example.training_project.data.remote.DTO

import com.google.gson.annotations.SerializedName

data class RequestTokenResponse(
    val success: Boolean,
    @SerializedName("request_token")
    val requestToken: String
)

data class LoginRequest(
    val username: String,
    val password: String,
    @SerializedName("request_token")
    val requestToken: String
)

data class CreateSessionRequest(
    @SerializedName("request_token")
    val requestToken: String
)

data class SessionResponse(
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String
)