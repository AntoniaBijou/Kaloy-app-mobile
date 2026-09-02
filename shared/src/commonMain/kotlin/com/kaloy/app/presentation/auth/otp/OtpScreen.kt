package com.kaloy.app.presentation.auth.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kaloy.app.core.session.AuthSessionManager
import org.koin.compose.koinInject

data class OtpScreen(
    val userId: Long,
    val email: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val repository = koinInject<com.kaloy.app.data.repository.AuthRepository>()
        val sessionManager = koinInject<AuthSessionManager>()
        val viewModel = remember { OtpViewModel(repository, userId) }
        val uiState by viewModel.uiState.collectAsState()

        var otpValue by remember { mutableStateOf("") }
        var verificationChannel by remember { mutableStateOf("EMAIL") }
        var resendCooldown by remember { mutableStateOf(0) }
        val focusRequester = remember { FocusRequester() }

        fun maskEmail(rawEmail: String): String {
            val (localPart, domainPart) = rawEmail.split("@", limit = 2)
            val maskedLocal = if (localPart.length <= 1) localPart else "${localPart.first()}***"
            return "$maskedLocal@$domainPart"
        }

        val headerText = if (verificationChannel == "SMS") {
            "Entrez le code à 6 chiffres envoyé par SMS au +33 6 ** ** ** 45"
        } else {
            "Entrez le code à 6 chiffres envoyé à ${maskEmail(email)}"
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        LaunchedEffect(resendCooldown > 0) {
            if (resendCooldown > 0) {
                delay(1000)
                resendCooldown -= 1
            }
        }

        LaunchedEffect(uiState) {
            if (uiState is OtpUiState.Resent) {
                viewModel.resetUiState()
            }
            if (uiState is OtpUiState.Success) {
                sessionManager.saveSession((uiState as OtpUiState.Success).response)
                val username = (uiState as OtpUiState.Success).response.email.substringBefore("@")
                navigator.replace(com.kaloy.app.presentation.home.HomeScreen(username = username))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Vérification",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            Text(
                text = headerText,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF4B5563),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = otpValue,
                onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) otpValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.focusRequester(focusRequester),
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        repeat(6) { index ->
                            val char = otpValue.getOrNull(index)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(52.dp)
                                    .border(
                                        width = 2.dp,
                                        color = if (otpValue.length == index)
                                            Color(0xFF8B5CF6)
                                        else
                                            Color(0xFF9CA3AF),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Text(
                                    text = char?.toString() ?: "",
                                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center,
                                    color = Color(0xFF111827)
                                )
                            }
                        }
                    }
                }
            )

            when (val state = uiState) {
                is OtpUiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                is OtpUiState.Resent -> Text(
                    text = "Code renvoyé avec succès.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
                is OtpUiState.Success -> {
                    Text(
                        text = "Compte vérifié !",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.verifyOtp(otpValue) },
                enabled = otpValue.length == 6 && uiState !is OtpUiState.Loading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
            ) {
                if (uiState is OtpUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Valider", fontWeight = FontWeight.SemiBold)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Vous n'avez pas reçu le code ?",
                    color = Color(0xFF4B5563),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = {
                        if (resendCooldown == 0) {
                            viewModel.resendOtp()
                            resendCooldown = 60
                        }
                    },
                    enabled = uiState !is OtpUiState.Loading && resendCooldown == 0
                ) {
                    val resendLabel = if (verificationChannel == "SMS") {
                        "Renvoyer le code par SMS"
                    } else {
                        "Renvoyer le code par email"
                    }
                    Text(
                        text = if (resendCooldown > 0) "$resendLabel (${resendCooldown}s)" else resendLabel,
                        color = Color(0xFF8B5CF6),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Préférez-vous recevoir le code par SMS ?",
                    color = Color(0xFF4B5563),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = {
                        verificationChannel = "SMS"
                        otpValue = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Recevoir par SMS",
                        color = Color(0xFF8B5CF6),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { navigator.pop() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(18.dp),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text("Retour", fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
            }
        }
    }
}
