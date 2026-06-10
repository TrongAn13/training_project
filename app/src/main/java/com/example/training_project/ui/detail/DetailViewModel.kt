package com.example.training_project.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.utils.Resource
import com.example.training_project.utils.ResourceProvider

class DetailViewModel(resourceProvider: ResourceProvider,private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    private val _movie = MutableLiveData<Resource<Movie>>()
    val movie: LiveData<Resource<Movie>> get() = _movie
    private var currentMovieId = -1L

    fun fetchMovieDetails(movieId: Long) {
        currentMovieId = movieId
        if (movieId == -1L) return

        executeApi(_movie) {
            useCases.getMovieDetails(movieId)
        }
    }
    fun retry() {
        if (currentMovieId != -1L) {
            fetchMovieDetails(currentMovieId)
        }
    }
}
