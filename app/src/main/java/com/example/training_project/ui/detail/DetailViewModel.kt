package com.example.training_project.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import com.example.training_project.utils.Resource
import com.example.training_project.ui.base.LoadingType

class DetailViewModel(application: Application) : BaseViewModel(application){
    private val repository = MovieRepository()
    private val _movie = MutableLiveData<Resource<Movie>>()

    val movie: LiveData<Resource<Movie>> get() = _movie
    private var currentMovieId = -1L

    fun fetchMovieDetails(movieId: Long) {
        currentMovieId = movieId
        if (movieId == -1L) return
        _movie.value = Resource.Loading
        executeApi(_movie, LoadingType.SHIMMER) {
            repository.getMovieDetailsFromApi(movieId)
        }
    }
    fun retry() {
        if (currentMovieId != -1L) {
            fetchMovieDetails(currentMovieId)
        }
    }
}
