package com.example.training_project.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import com.example.training_project.utils.Resource
import com.example.training_project.utils.executeApi

class DetailViewModel: ViewModel(){
    private val repository = MovieRepository()
    private val _movie = MutableLiveData<Resource<Movie>>()

    val movie: LiveData<Resource<Movie>> get() = _movie
    private var currentMovieId = -1L

    fun fetchMovieDetails(movieId: Long) {
        currentMovieId = movieId
        if (movieId == -1L) return
        executeApi(_movie) {
            repository.getMovieDetailsFromApi(movieId)
        }
    }
    fun retry() {
        if (currentMovieId != -1L) {
            fetchMovieDetails(currentMovieId)
        }
    }
}