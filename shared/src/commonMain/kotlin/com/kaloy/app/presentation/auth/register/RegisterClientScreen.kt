package com.kaloy.app.presentation.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kaloy.app.data.repository.AuthRepository
import com.kaloy.app.presentation.auth.otp.OtpScreen
import org.koin.compose.koinInject

data class RegisterClientScreen(
    val email: String,
    val phone: String,
    val password: String,
    val otpChannel: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val repository = koinInject<AuthRepository>()
        val viewModel = remember { RegisterViewModel(repository) }
        val uiState by viewModel.uiState.collectAsState()

        DisposableEffect(viewModel) {
            onDispose { viewModel.dispose() }
        }

        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }

        LaunchedEffect(uiState) {
            if (uiState is RegisterUiState.Success) {
                val response = (uiState as RegisterUiState.Success).response
                viewModel.resetUiState()
                navigator.push(OtpScreen(userId = response.userId, email = email))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Informations personnelles",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ces champs sont optionnels. Vous pouvez les remplir plus tard.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nom d'utilisateur") },
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState is RegisterUiState.Error) {
                Text(
                    text = (uiState as RegisterUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.registerClient(
                        email = email,
                        password = password,
                        phone = phone.ifBlank { null },
                        otpChannel = otpChannel,
                        firstName = firstName.ifBlank { null },
                        lastName = lastName.ifBlank { null },
                        username = username.ifBlank { null }
                    )
                },
                enabled = uiState !is RegisterUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState is RegisterUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Créer mon compte")
                }
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
