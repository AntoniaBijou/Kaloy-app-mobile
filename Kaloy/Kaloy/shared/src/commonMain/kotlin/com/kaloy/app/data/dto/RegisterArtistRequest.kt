package com.kaloy.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterArtistRequest(
    val email: String,
    val password: String,
    val artistType: String,
    val stageName: String,
    val phone: String? = null,
    val activeSinceYear: Int? = null,
    val bio: String? = null,
    val photoUrl: String? = null,
    val otpChannel: String = "EMAIL"
)
