package com.kaloy.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.kaloy.app.presentation.auth.welcome.WelcomeScreen
import com.kaloy.app.ui.theme.KaloyTheme

@Composable
fun App() {
    KaloyTheme {
        Navigator(screen = WelcomeScreen())
    }
}