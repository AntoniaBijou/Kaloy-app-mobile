package com.kaloy.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class OtpVerifyRequest(
    val userId: Long,
    val code: String
)
