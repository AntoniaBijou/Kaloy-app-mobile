package com.kaloy.app.presentation.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Créer un compte",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Text(
                    text = "Rejoignez la communauté Kaloy et accordez votre identité musicale.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4B5563),
                    textAlign = TextAlign.Start
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; emailError = "" },
                            label = { Text("Email *") },
                            isError = emailError.isNotEmpty(),
                            supportingText = { if (emailError.isNotEmpty()) Text(emailError) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF111827),
                                unfocusedTextColor = Color(0xFF111827),
                                focusedBorderColor = Color(0xFF8B5CF6),
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = Color(0xFF8B5CF6),
                                focusedLabelColor = Color(0xFF8B5CF6),
                                unfocusedLabelColor = Color(0xFF4B5563),
                                errorBorderColor = Color(0xFFE11D48)
                            )
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Numéro de téléphone / Contact") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF111827),
                                unfocusedTextColor = Color(0xFF111827),
                                focusedBorderColor = Color(0xFF8B5CF6),
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = Color(0xFF8B5CF6),
                                focusedLabelColor = Color(0xFF8B5CF6),
                                unfocusedLabelColor = Color(0xFF4B5563)
                            )
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; passwordError = "" },
                            label = { Text("Mot de passe *") },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = passwordError.isNotEmpty(),
                            supportingText = { if (passwordError.isNotEmpty()) Text(passwordError) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF111827),
                                unfocusedTextColor = Color(0xFF111827),
                                focusedBorderColor = Color(0xFF8B5CF6),
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = Color(0xFF8B5CF6),
                                focusedLabelColor = Color(0xFF8B5CF6),
                                unfocusedLabelColor = Color(0xFF4B5563),
                                errorBorderColor = Color(0xFFE11D48)
                            )
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Type de compte",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = accountType == "CLIENT",
                                onClick = { accountType = "CLIENT" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF8B5CF6),
                                    unselectedColor = Color(0xFF6B7280)
                                )
                            )
                            Text("Client", color = Color(0xFF111827), modifier = Modifier.padding(end = 16.dp))
                            RadioButton(
                                selected = accountType == "ARTISTE",
                                onClick = { accountType = "ARTISTE" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF8B5CF6),
                                    unselectedColor = Color(0xFF6B7280)
                                )
                            )
                            Text("Artiste", color = Color(0xFF111827))
                        }

                        HorizontalDivider(color = Color(0xFFD1D5DB))

                        Text(
                            text = "Canal de vérification",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = otpChannel == "EMAIL",
                                onClick = { otpChannel = "EMAIL" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF8B5CF6),
                                    unselectedColor = Color(0xFF6B7280)
                                )
                            )
                            Text("Email", color = Color(0xFF111827), modifier = Modifier.padding(end = 16.dp))
                            RadioButton(
                                selected = otpChannel == "SMS",
                                onClick = { otpChannel = "SMS" },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF8B5CF6),
                                    unselectedColor = Color(0xFF6B7280)
                                )
                            )
                            Text("SMS", color = Color(0xFF111827))
                        }

                        if (otpChannel == "SMS" && phone.isBlank()) {
                            Text(
                                text = "Un numéro de téléphone est requis pour recevoir le code OTP par SMS.",
                                color = Color(0xFFB91C1C),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

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
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
                ) {
                    Text("Continuer", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
