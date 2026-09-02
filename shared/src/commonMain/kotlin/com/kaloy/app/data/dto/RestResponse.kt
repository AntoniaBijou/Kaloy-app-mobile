package com.kaloy.app.data.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class RestResponse(
    val status: Int = 0,
    val message: String = "",
    val data: JsonElement? = null,
    val errors: JsonElement? = null,
    val timestamp: String = ""
)
