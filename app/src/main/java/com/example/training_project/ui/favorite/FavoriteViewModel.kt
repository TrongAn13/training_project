package com.example.training_project.ui.favorite

import com.example.domain.model.Movie
import com.example.domain.usecase.MovieUseCases
import com.example.uicompose.ResourceProvider
import com.example.uicompose.base.BaseComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FavoriteUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
class FavoriteViewModel(resourceProvider: ResourceProvider, private val useCases: MovieUseCases) : BaseComposeViewModel(resourceProvider) {
    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    fun getFavoriteMovies() {
        executeApiState(
            onLoading = { loading ->
                _uiState.update { it.copy(
                    isLoading = loading,
                    error = null
                ) }
            },
            onSuccess = { movies ->
                _uiState.update { it.copy(
                    movies = movies,
                    error = null
                ) }
            },
            onError = { message ->
                _uiState.update { it.copy(
                    error = message
                    )
                }
            }
        ) {
            useCases.getFavoriteMovies()
        }
    }
}