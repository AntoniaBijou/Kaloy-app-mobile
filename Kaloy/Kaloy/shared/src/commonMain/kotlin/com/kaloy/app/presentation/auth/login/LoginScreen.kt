package com.kaloy.app.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kaloy.app.core.error.UserErrorMessages
import com.kaloy.app.core.session.AuthSessionManager
import com.kaloy.app.data.dto.LoginRequest
import com.kaloy.app.data.repository.AuthRepository
import com.kaloy.app.presentation.auth.register.RegisterStep1Screen
import com.kaloy.app.presentation.home.HomeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val email: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(
    private val repository: AuthRepository,
    private val sessionManager: AuthSessionManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        scope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val response = repository.login(LoginRequest(email.trim(), password))
                sessionManager.saveSession(response)
                _uiState.value = LoginUiState.Success(response.email)
            } catch (e: Exception) {
                val message = UserErrorMessages.fromThrowable(e)
                _uiState.value = LoginUiState.Error(message)
            }
        }
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Idle
    }

    fun dispose() = scope.cancel()
}

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val repository = koinInject<AuthRepository>()
        val sessionManager = koinInject<AuthSessionManager>()
        val viewModel = remember { LoginViewModel(repository, sessionManager) }
        val uiState by viewModel.uiState.collectAsState()

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var emailError by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf("") }

        LaunchedEffect(uiState) {
            if (uiState is LoginUiState.Success) {
                val username = (uiState as LoginUiState.Success).email.substringBefore("@")
                navigator.replace(HomeScreen(username = username))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Connexion",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )

                        Text(
                            text = "Bienvenue à nouveau sur Kaloy.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF4B5563)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; emailError = "" },
                            label = { Text("Email") },
                            isError = emailError.isNotEmpty(),
                            supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF111827),
                                unfocusedTextColor = Color(0xFF111827),
                                focusedBorderColor = Color(0xFF8B5CF6),
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedLabelColor = Color(0xFF8B5CF6),
                                unfocusedLabelColor = Color(0xFF4B5563),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorBorderColor = Color(0xFFE11D48)
                            )
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; passwordError = "" },
                            label = { Text("Mot de passe") },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = passwordError.isNotEmpty(),
                            supportingText = { if (passwordError.isNotEmpty()) Text(passwordError) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF111827),
                                unfocusedTextColor = Color(0xFF111827),
                                focusedBorderColor = Color(0xFF8B5CF6),
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedLabelColor = Color(0xFF8B5CF6),
                                unfocusedLabelColor = Color(0xFF4B5563),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                errorBorderColor = Color(0xFFE11D48)
                            )
                        )

                        when (val state = uiState) {
                            is LoginUiState.Error -> {
                                Text(
                                    text = state.message,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            else -> Unit
                        }

                        Button(
                            onClick = {
                                var valid = true
                                if (email.trim().isBlank() || !email.contains("@")) {
                                    emailError = "Email invalide"
                                    valid = false
                                }
                                if (password.length < 6) {
                                    passwordError = "Mot de passe trop court"
                                    valid = false
                                }
                                if (!valid) return@Button
                                viewModel.login(email, password)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                            enabled = uiState !is LoginUiState.Loading
                        ) {
                            if (uiState is LoginUiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.height(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                            } else {
                                Text("Se connecter")
                            }
                        }

                        OutlinedButton(
                            onClick = { navigator.push(RegisterStep1Screen()) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text("Créer un compte")
                        }
                    }
                }
            }
        }
    }
}
