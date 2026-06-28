package com.example.training_project.ui.favorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteRoute(
    onMovieClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFavoriteMovies()
    }

    FavoriteScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onMovieClick = { movie ->
            onMovieClick(movie.id ?: -1L)
        },
        modifier = modifier
    )
}