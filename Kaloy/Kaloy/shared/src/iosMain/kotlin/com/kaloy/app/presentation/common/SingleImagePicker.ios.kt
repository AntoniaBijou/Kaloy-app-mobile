package com.kaloy.app.presentation.common

import androidx.compose.runtime.Composable

@Composable
actual fun rememberSingleImagePicker(
    onImageSelected: (String?) -> Unit
): () -> Unit {
    return {
        onImageSelected(null)
    }
}
