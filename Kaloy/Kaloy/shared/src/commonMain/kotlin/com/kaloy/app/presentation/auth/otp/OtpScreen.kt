package com.kaloy.app.presentation.auth.otp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject

data class OtpScreen(
    val userId: Long,
    val email: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val repository = koinInject<com.kaloy.app.data.repository.AuthRepository>()
        val viewModel = remember { OtpViewModel(repository, userId) }
        val uiState by viewModel.uiState.collectAsState()

        var otpValue by remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        LaunchedEffect(uiState) {
            if (uiState is OtpUiState.Resent) {
                viewModel.resetUiState()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Vérification",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Entrez le code à 6 chiffres envoyé à $email",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = otpValue,
                onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) otpValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.focusRequester(focusRequester),
                decorationBox = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(6) { index ->
                            val char = otpValue.getOrNull(index)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .border(
                                        width = 2.dp,
                                        color = if (otpValue.length == index)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.outline,
                                        shape = MaterialTheme.shapes.small
                                    )
                            ) {
                                Text(
                                    text = char?.toString() ?: "",
                                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                                    textAlign = TextAlign.Center
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
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState is OtpUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Valider")
                }
            }

            TextButton(
                onClick = { viewModel.resendOtp() },
                enabled = uiState !is OtpUiState.Loading
            ) {
                Text("Renvoyer le code")
            }

            OutlinedButton(
                onClick = { navigator.pop() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Retour")
            }
        }
    }
}
