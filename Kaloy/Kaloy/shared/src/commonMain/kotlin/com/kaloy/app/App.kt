package com.kaloy.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.kaloy.app.di.appModule
import com.kaloy.app.presentation.auth.register.RegisterStep1Screen
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        MaterialTheme {
            Navigator(RegisterStep1Screen())
        }
    }
}
