package com.kaloy.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val userId: Long,
    val email: String,
    val message: String,
    val status: String
)
