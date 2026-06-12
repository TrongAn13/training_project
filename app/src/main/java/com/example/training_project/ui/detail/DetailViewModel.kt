package com.example.training_project.ui.detail

import com.example.domain.model.Cast
import com.example.domain.model.Movie
import com.example.domain.model.Review
import com.example.domain.usecase.MovieUseCases
import com.example.ui.ResourceProvider
import com.example.uicompose.base.BaseComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class DetailUiState(
    val movie: Movie? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DetailViewModel(resourceProvider: ResourceProvider,private val useCases: MovieUseCases) : BaseComposeViewModel(resourceProvider) {
    private val _detailUiState = MutableStateFlow(DetailUiState())
    val detailUiState = _detailUiState.asStateFlow()
    private var hasIncreasedViewCount = false
    private var currentMovieId = -1L

    fun fetchMovieDetails(movieId: Long) {
        if(currentMovieId == movieId && _detailUiState.value.movie != null) return
        currentMovieId = movieId
        executeApiState(
            onLoading = { loading ->
                _detailUiState.update {
                    it.copy(
                    isLoading = loading,
                    error = null
                    )
                }
            },
            onSuccess = { movie ->
                _detailUiState.update {
                    it.copy(
                        movie = movie,
                        error = null
                    )
                }
                checkIsFavorite(movieId)
                increaseDetailViewCountOnce(movieId)
            },
            onError = { message ->
                _detailUiState.update {
                    it.copy(
                        error = message
                    )
                }
            }
        ) {
            useCases.getMovieDetails(movieId)
        }
    }

    fun checkIsFavorite(movieId: Long) {
        executeApiState(
            onLoading = {},
            onSuccess = { saved ->
                _detailUiState.value = _detailUiState.value.copy(
                    isFavorite = saved
                )
            },
            onError = { message ->
                _detailUiState.value = _detailUiState.value.copy(
                    error = message
                )
            }
        ) {
            useCases.isMovieSaved(movieId)
        }
    }
    fun toggleFavorite() {
        val movie = _detailUiState.value.movie ?: return
        executeApiState(
            onLoading = {},
            onSuccess = { newFavoriteState ->
                _detailUiState.value = _detailUiState.value.copy(
                    isFavorite = newFavoriteState
                )
            },
            onError = { message ->
                _detailUiState.value = _detailUiState.value.copy(
                    error = message
                )
            }
        ) {
            val saved = useCases.isMovieSaved(movie.id)
            if (saved) {
                useCases.deleteFavoriteMovie(movie.id)
                false
            } else {
                useCases.saveFavoriteMovie(movie)
                true
            }
        }
    }
    private fun increaseDetailViewCountOnce(movieId: Long) {
        if (hasIncreasedViewCount) return
        hasIncreasedViewCount = true

        executeApi(
            showGlobalLoading = false,
            onSuccess = {},
            onError = {}
        ) {
            useCases.increaseDetailViewCount(movieId)
        }
    }

    fun retry() {
        if (currentMovieId != -1L) {
            fetchMovieDetails(currentMovieId)
        }
    }
}
