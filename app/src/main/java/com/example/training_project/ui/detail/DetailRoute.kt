package com.example.training_project.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailRoute(
    movieId: Long,
    viewModel: DetailViewModel = koinViewModel() { parametersOf(movieId) },
    modifier: Modifier,
    onBackClick: () -> Unit,

    ) {
    val uiState by viewModel.detailUiState.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        viewModel.fetchMovieDetails(movieId)
    }

    DetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onBookmarkClick = {
            viewModel.toggleFavorite()
        },
        onRetryClick = {
            viewModel.retry()
        },
        modifier = modifier
    )
}