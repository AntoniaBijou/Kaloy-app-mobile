package com.kaloy.app.presentation.common

import androidx.compose.runtime.Composable

@Composable
expect fun rememberSingleImagePicker(
    onImageSelected: (String?) -> Unit
): () -> Unit
