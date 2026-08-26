package com.kaloy.app.presentation.auth.register

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kaloy.app.data.dto.RegisterArtistRequest
import com.kaloy.app.data.dto.RegisterClientRequest
import com.kaloy.app.data.dto.RegisterResponse
import com.kaloy.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterFormState(
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val otpChannel: String = "EMAIL",
    val accountType: String = "CLIENT",
    // Client fields
    val firstName: String = "",
    val lastName: String = "",
    val username: String = "",
    // Artist fields
    val artistType: String = "SOLO",
    val stageName: String = "",
    val activeSinceYear: String = "",
    val bio: String = "",
    val photoUrl: String = ""
)

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val response: RegisterResponse) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel(private val repository: AuthRepository) : ScreenModel {

    private val _formState = MutableStateFlow(RegisterFormState())
    val formState: StateFlow<RegisterFormState> = _formState.asStateFlow()

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateForm(update: RegisterFormState.() -> RegisterFormState) {
        _formState.value = _formState.value.update()
    }

    fun resetUiState() {
        _uiState.value = RegisterUiState.Idle
    }

    fun registerClient() {
        val form = _formState.value
        screenModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val response = repository.registerClient(
                    RegisterClientRequest(
                        email = form.email,
                        password = form.password,
                        phone = form.phone.ifBlank { null },
                        firstName = form.firstName.ifBlank { null },
                        lastName = form.lastName.ifBlank { null },
                        username = form.username.ifBlank { null },
                        otpChannel = form.otpChannel
                    )
                )
                _uiState.value = RegisterUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun registerArtist() {
        val form = _formState.value
        screenModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                val response = repository.registerArtist(
                    RegisterArtistRequest(
                        email = form.email,
                        password = form.password,
                        artistType = form.artistType,
                        stageName = form.stageName,
                        phone = form.phone.ifBlank { null },
                        activeSinceYear = form.activeSinceYear.toIntOrNull(),
                        bio = form.bio.ifBlank { null },
                        photoUrl = form.photoUrl.ifBlank { null },
                        otpChannel = form.otpChannel
                    )
                )
                _uiState.value = RegisterUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }
}
