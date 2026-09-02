package com.kaloy.app.presentation.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

data class RegisterArtistTypeScreen(
    val email: String,
    val phone: String,
    val password: String,
    val otpChannel: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var artistType by remember { mutableStateOf("SOLO") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Type d'artiste",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Êtes-vous un artiste solo ou un groupe ?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                onClick = { artistType = "SOLO" },
                colors = CardDefaults.cardColors(
                    containerColor = if (artistType == "SOLO")
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = artistType == "SOLO", onClick = { artistType = "SOLO" })
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Solo", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Artiste individuel",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Card(
                onClick = { artistType = "GROUP" },
                colors = CardDefaults.cardColors(
                    containerColor = if (artistType == "GROUP")
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = artistType == "GROUP", onClick = { artistType = "GROUP" })
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Groupe", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Groupe musical ou collectif",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    navigator.push(
                        RegisterArtistDetailsScreen(
                            email = email,
                            phone = phone,
                            password = password,
                            otpChannel = otpChannel,
                            artistType = artistType
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Suivant")
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
