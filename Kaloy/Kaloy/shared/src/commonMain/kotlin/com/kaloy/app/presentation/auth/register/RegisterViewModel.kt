package com.kaloy.app.presentation.auth.register

import com.kaloy.app.data.dto.RegisterArtistRequest
import com.kaloy.app.data.dto.RegisterClientRequest
import com.kaloy.app.data.dto.RegisterResponse
import com.kaloy.app.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val response: RegisterResponse) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel(private val repository: AuthRepository) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun resetUiState() {
        _uiState.value = RegisterUiState.Idle
    }

    fun registerClient(
        email: String,
        password: String,
        phone: String?,
        otpChannel: String,
        firstName: String?,
        lastName: String?,
        username: String?
    ) {
        scope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val response = repository.registerClient(
                    RegisterClientRequest(
                        email = email,
                        password = password,
                        phone = phone?.ifBlank { null },
                        firstName = firstName?.ifBlank { null },
                        lastName = lastName?.ifBlank { null },
                        username = username?.ifBlank { null },
                        otpChannel = otpChannel
                    )
                )
                _uiState.value = RegisterUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun registerArtist(
        email: String,
        password: String,
        phone: String?,
        otpChannel: String,
        artistType: String,
        stageName: String,
        activeSinceYear: String,
        bio: String,
        photoUrl: String
    ) {
        scope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val response = repository.registerArtist(
                    RegisterArtistRequest(
                        email = email,
                        password = password,
                        artistType = artistType,
                        stageName = stageName,
                        phone = phone?.ifBlank { null },
                        activeSinceYear = activeSinceYear.toIntOrNull(),
                        bio = bio.ifBlank { null },
                        photoUrl = photoUrl.ifBlank { null },
                        otpChannel = otpChannel
                    )
                )
                _uiState.value = RegisterUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun dispose() = scope.cancel()
}
