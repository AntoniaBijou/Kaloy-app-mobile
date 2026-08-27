package com.kaloy.app.core.network

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// Émulateur Android  → BASE_URL = "http://10.0.2.2:8087/mozika"
const val BASE_URL = "http://172.16.0.25:8087/mozika"

fun createHttpClient(): HttpClient = HttpClient {
    expectSuccess = false  // on gère les erreurs HTTP manuellement dans le repository
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(Logging) {
        level = LogLevel.BODY
    }
}
