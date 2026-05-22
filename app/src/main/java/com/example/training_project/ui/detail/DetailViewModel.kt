package com.example.training_project.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.ui.base.LoadingType
import com.example.training_project.utils.Resource
import com.example.training_project.utils.ResourceProvider

class DetailViewModel(resourceProvider: ResourceProvider,private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    private val _movie = MutableLiveData<Resource<Movie>>()
    val movie: LiveData<Resource<Movie>> get() = _movie
    val isFavorite = MutableLiveData<Resource<Boolean>>()
    private var currentMovieId = -1L

    val increaseDetailViewCount = MutableLiveData<Resource<Unit>>()

    fun fetchMovieDetails(movieId: Long) {
        currentMovieId = movieId
        if (movieId == -1L) return

        executeApi(_movie) {
            useCases.getMovieDetails(movieId)
        }
    }
    fun checkIsFavorite(movieId: Long) {
        executeApi(isFavorite, LoadingType.NONE){
            useCases.isMovieSaved(movieId)
        }
    }
    fun toggleFavorite(movie: Movie) {
        executeApi(isFavorite, LoadingType.NONE) {
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
    fun increaseDetailViewCount(movieId: Long) {
        executeApi(increaseDetailViewCount, LoadingType.NONE) {
            useCases.increaseDetailViewCount(movieId)
        }
    }
    fun retry() {
        if (currentMovieId != -1L) {
            fetchMovieDetails(currentMovieId)
        }
    }
}
