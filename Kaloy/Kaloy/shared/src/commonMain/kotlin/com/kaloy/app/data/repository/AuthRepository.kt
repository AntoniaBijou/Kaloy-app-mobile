package com.kaloy.app.data.repository

import com.kaloy.app.data.dto.*

interface AuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun registerClient(request: RegisterClientRequest): RegisterResponse
    suspend fun registerArtist(request: RegisterArtistRequest): RegisterResponse
    suspend fun verifyOtp(request: OtpVerifyRequest): AuthResponse
    suspend fun resendOtp(request: ResendOtpRequest): String
}
