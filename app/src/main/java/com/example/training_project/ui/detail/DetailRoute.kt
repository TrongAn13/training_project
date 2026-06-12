package com.example.training_project.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetailRoute(
    viewModel: DetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.detailUiState.collectAsStateWithLifecycle()

    DetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onBookmarkClick = {
            viewModel.toggleFavorite()
        },
        onRetryClick = {
            viewModel.retry()
        }
    )
}