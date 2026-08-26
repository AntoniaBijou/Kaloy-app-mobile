package com.kaloy.app.data.repository

import com.kaloy.app.core.network.BASE_URL
import com.kaloy.app.data.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {

    override suspend fun registerClient(request: RegisterClientRequest): RegisterResponse {
        val rest = client.post("$BASE_URL/auth/register/client") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<RestResponse>()
        return Json.decodeFromJsonElement(RegisterResponse.serializer(), rest.data!!)
    }

    override suspend fun registerArtist(request: RegisterArtistRequest): RegisterResponse {
        val rest = client.post("$BASE_URL/auth/register/artist") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<RestResponse>()
        return Json.decodeFromJsonElement(RegisterResponse.serializer(), rest.data!!)
    }

    override suspend fun verifyOtp(request: OtpVerifyRequest): AuthResponse {
        val rest = client.post("$BASE_URL/auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<RestResponse>()
        return Json.decodeFromJsonElement(AuthResponse.serializer(), rest.data!!)
    }

    override suspend fun resendOtp(request: ResendOtpRequest): String {
        val rest = client.post("$BASE_URL/auth/resend-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<RestResponse>()
        return rest.message
    }
}
