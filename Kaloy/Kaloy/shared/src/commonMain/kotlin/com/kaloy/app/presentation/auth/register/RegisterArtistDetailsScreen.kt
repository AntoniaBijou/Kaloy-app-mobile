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
import com.kaloy.app.presentation.common.rememberSingleImagePicker
import org.koin.compose.koinInject

data class RegisterArtistDetailsScreen(
    val email: String,
    val phone: String,
    val password: String,
    val otpChannel: String,
    val artistType: String
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

        var stageName by remember { mutableStateOf("") }
        var stageNameError by remember { mutableStateOf("") }
        var activeSinceYear by remember { mutableStateOf("") }
        var bio by remember { mutableStateOf("") }
        var photoUrl by remember { mutableStateOf("") }
        val pickProfilePhoto = rememberSingleImagePicker { uri ->
            photoUrl = uri.orEmpty()
        }

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
                text = "Profil artiste",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = stageName,
                onValueChange = { stageName = it; stageNameError = "" },
                label = { Text("Nom de scène *") },
                isError = stageNameError.isNotEmpty(),
                supportingText = { if (stageNameError.isNotEmpty()) Text(stageNameError) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = activeSinceYear,
                onValueChange = { activeSinceYear = it },
                label = { Text("Actif depuis (année)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Biographie") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { pickProfilePhoto() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (photoUrl.isBlank()) "Téléverser une photo de profil" else "Changer la photo de profil")
            }

            if (photoUrl.isNotBlank()) {
                Text(
                    text = "Photo sélectionnée",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

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
                    if (stageName.isBlank()) {
                        stageNameError = "Le nom de scène est requis"
                        return@Button
                    }
                    viewModel.registerArtist(
                        email = email,
                        password = password,
                        phone = phone.ifBlank { null },
                        otpChannel = otpChannel,
                        artistType = artistType,
                        stageName = stageName,
                        activeSinceYear = activeSinceYear,
                        bio = bio,
                        photoUrl = photoUrl
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
