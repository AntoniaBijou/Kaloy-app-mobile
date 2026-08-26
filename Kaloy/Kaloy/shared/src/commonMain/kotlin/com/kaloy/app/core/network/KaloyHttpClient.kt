package com.kaloy.app.core.network

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// Émulateur Android → 10.0.2.2 pointe vers localhost de la machine hôte
// Device réel sur le même réseau → remplacer par l'IP de votre machine (ex: 192.168.1.X)
const val BASE_URL = "http://10.0.2.2:8087/mozika"

fun createHttpClient(): HttpClient = HttpClient {
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
