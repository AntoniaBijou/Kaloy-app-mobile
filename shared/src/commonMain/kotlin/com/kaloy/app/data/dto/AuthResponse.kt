package com.kaloy.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val userId: Long,
    val email: String,
    val role: String
)
