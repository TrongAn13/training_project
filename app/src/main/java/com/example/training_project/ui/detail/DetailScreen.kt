package com.example.training_project.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.uicompose.base.AppLoading

@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onRetryClick: () -> Unit
) {
    when {
        uiState.isLoading -> {
            AppLoading()
        }
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.error)
            }
        }
        uiState.movie != null -> {
            DetailContent(
                movie = uiState.movie,
                isFavorite = uiState.isFavorite,
                onBackClick = onBackClick,
                onBookmarkClick = onBookmarkClick
            )
        }
    }
}