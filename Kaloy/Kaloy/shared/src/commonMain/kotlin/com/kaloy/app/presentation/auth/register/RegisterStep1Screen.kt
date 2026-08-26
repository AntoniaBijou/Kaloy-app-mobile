package com.kaloy.app.presentation.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class RegisterStep1Screen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var email by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var accountType by remember { mutableStateOf("CLIENT") }
        var otpChannel by remember { mutableStateOf("EMAIL") }
        var emailError by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Créer un compte",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = "" },
                label = { Text("Email *") },
                isError = emailError.isNotEmpty(),
                supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Téléphone") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = "" },
                label = { Text("Mot de passe *") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError.isNotEmpty(),
                supportingText = { if (passwordError.isNotEmpty()) Text(passwordError) },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Type de compte", style = MaterialTheme.typography.labelLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = accountType == "CLIENT",
                    onClick = { accountType = "CLIENT" }
                )
                Text("Client", modifier = Modifier.padding(end = 16.dp))
                RadioButton(
                    selected = accountType == "ARTISTE",
                    onClick = { accountType = "ARTISTE" }
                )
                Text("Artiste")
            }

            Text("Recevoir le code OTP par", style = MaterialTheme.typography.labelLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = otpChannel == "EMAIL",
                    onClick = { otpChannel = "EMAIL" }
                )
                Text("Email", modifier = Modifier.padding(end = 16.dp))
                RadioButton(
                    selected = otpChannel == "SMS",
                    onClick = { otpChannel = "SMS" }
                )
                Text("SMS")
            }

            if (otpChannel == "SMS" && phone.isBlank()) {
                Text(
                    text = "Un numéro de téléphone est requis pour recevoir l'OTP par SMS.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    var valid = true
                    if (email.isBlank() || !email.contains("@")) {
                        emailError = "Email invalide"
                        valid = false
                    }
                    if (password.length < 6) {
                        passwordError = "Minimum 6 caractères"
                        valid = false
                    }
                    if (otpChannel == "SMS" && phone.isBlank()) {
                        valid = false
                    }
                    if (!valid) return@Button

                    if (accountType == "CLIENT") {
                        navigator.push(
                            RegisterClientScreen(
                                email = email,
                                phone = phone,
                                password = password,
                                otpChannel = otpChannel
                            )
                        )
                    } else {
                        navigator.push(
                            RegisterArtistTypeScreen(
                                email = email,
                                phone = phone,
                                password = password,
                                otpChannel = otpChannel
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Suivant")
            }
        }
    }
}
