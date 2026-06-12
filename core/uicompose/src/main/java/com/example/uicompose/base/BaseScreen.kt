package com.example.uicompose.base

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun BaseScreen(
    baseUiState: BaseUiState,
    onClearError: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        content()
        if (baseUiState.isLoading) {
            AppLoading()
        }
        baseUiState.error?.let { message ->
            LaunchedEffect(message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                onClearError()
            }
        }
    }
}