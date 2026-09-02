package com.kaloy.app.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResendOtpRequest(val userId: Long)
