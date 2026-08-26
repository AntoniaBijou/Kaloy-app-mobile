package com.kaloy.app.presentation.auth.otp

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kaloy.app.data.dto.AuthResponse
import com.kaloy.app.data.dto.OtpVerifyRequest
import com.kaloy.app.data.dto.ResendOtpRequest
import com.kaloy.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OtpUiState {
    object Idle : OtpUiState()
    object Loading : OtpUiState()
    data class Success(val response: AuthResponse) : OtpUiState()
    data class Error(val message: String) : OtpUiState()
    object Resent : OtpUiState()
}

class OtpViewModel(
    private val repository: AuthRepository,
    val userId: Long
) : ScreenModel {

    private val _uiState = MutableStateFlow<OtpUiState>(OtpUiState.Idle)
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()

    fun verifyOtp(code: String) {
        screenModelScope.launch {
            _uiState.value = OtpUiState.Loading
            try {
                val response = repository.verifyOtp(OtpVerifyRequest(userId = userId, code = code))
                _uiState.value = OtpUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = OtpUiState.Error(e.message ?: "Code invalide ou expiré")
            }
        }
    }

    fun resendOtp() {
        screenModelScope.launch {
            _uiState.value = OtpUiState.Loading
            try {
                repository.resendOtp(ResendOtpRequest(userId = userId))
                _uiState.value = OtpUiState.Resent
            } catch (e: Exception) {
                _uiState.value = OtpUiState.Error(e.message ?: "Impossible de renvoyer le code")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = OtpUiState.Idle
    }
}
