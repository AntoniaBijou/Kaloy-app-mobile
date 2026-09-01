package com.kaloy.app.core.network

import com.kaloy.app.core.session.AuthSessionManager
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// Émulateur Android  → BASE_URL = "http://10.0.2.2:8087/mozika"
// Appareil physique sur le même Wi‑Fi → BASE_URL = "http://192.168.1.191:8087/mozika"
const val BASE_URL = "http://192.168.1.191:8087/mozika"

fun createHttpClient(sessionManager: AuthSessionManager): HttpClient = HttpClient {
    expectSuccess = false  // on gère les erreurs HTTP manuellement dans le repository
    defaultRequest {
        val token = sessionManager.getToken()
        if (!token.isNullOrBlank()) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
    install(HttpCallValidator) {
        handleResponseExceptionWithRequest { cause, _ ->
            val status = (cause as? ClientRequestException)?.response?.status
            if (status == HttpStatusCode.Unauthorized) {
                sessionManager.clear()
            }
        }
    }
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
