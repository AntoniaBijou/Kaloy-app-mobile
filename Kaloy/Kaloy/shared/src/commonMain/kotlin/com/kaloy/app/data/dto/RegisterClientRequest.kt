package com.kaloy.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterClientRequest(
    val email: String,
    val password: String,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val otpChannel: String = "EMAIL"
)
