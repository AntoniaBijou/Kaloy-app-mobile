package com.kaloy.app

import com.kaloy.app.data.dto.LoginRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthFlowCommonTest {
    @Test
    fun loginRequest_shouldKeepCredentials() {
        val request = LoginRequest(
            email = "alice@kaloy.app",
            password = "secret123"
        )

        assertEquals("alice@kaloy.app", request.email)
        assertEquals("secret123", request.password)
    }
}