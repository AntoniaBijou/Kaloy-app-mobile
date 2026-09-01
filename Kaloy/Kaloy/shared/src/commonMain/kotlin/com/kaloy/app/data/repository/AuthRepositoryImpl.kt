package com.kaloy.app.data.repository

import com.kaloy.app.core.error.UserErrorMessages
import com.kaloy.app.core.session.AuthSessionManager
import com.kaloy.app.core.network.BASE_URL
import com.kaloy.app.data.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: AuthSessionManager
) : AuthRepository {

    private suspend inline fun <reified T> HttpResponse.decodeData(): T {
        val rest = body<RestResponse>()
        if (status.value == 401) {
            sessionManager.clear()
        }
        if (status.value >= 400) {
            throw Exception(UserErrorMessages.fromRawMessage(rest.message))
        }
        val data = rest.data ?: throw Exception("Une erreur est survenue. Veuillez réessayer.")
        return Json.decodeFromJsonElement(kotlinx.serialization.serializer<T>(), data)
    }

    override suspend fun login(request: LoginRequest): AuthResponse {
        val response = client.post("$BASE_URL/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        val rest = response.body<RestResponse>()
        if (response.status.value == 401) {
            sessionManager.clear()
            throw Exception(UserErrorMessages.fromRawMessage(rest.message))
        }
        if (response.status.value >= 400) {
            throw Exception(UserErrorMessages.fromRawMessage(rest.message))
        }

        val data = rest.data ?: throw Exception("Une erreur est survenue. Veuillez réessayer.")
        return Json.decodeFromJsonElement(kotlinx.serialization.serializer<AuthResponse>(), data)
    }

    override suspend fun registerClient(request: RegisterClientRequest): RegisterResponse {
        return client.post("$BASE_URL/auth/register/client") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.decodeData()
    }

    override suspend fun registerArtist(request: RegisterArtistRequest): RegisterResponse {
        return client.post("$BASE_URL/auth/register/artist") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.decodeData()
    }

    override suspend fun verifyOtp(request: OtpVerifyRequest): AuthResponse {
        return client.post("$BASE_URL/auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.decodeData()
    }

    override suspend fun resendOtp(request: ResendOtpRequest): String {
        val rest = client.post("$BASE_URL/auth/resend-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<RestResponse>()
        return rest.message
    }
}
