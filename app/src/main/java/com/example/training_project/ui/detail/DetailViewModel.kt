package com.example.training_project.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import kotlinx.coroutines.launch


class DetailViewModel: ViewModel(){
    private val repository = MovieRepository()
    private val _movie = MutableLiveData<Movie>()

    val movie: LiveData<Movie> get() = _movie

    val errorMessage = MutableLiveData<String>()

    fun fetchMovieDetails(movieId: Long) {
        if (movieId == -1L) return

        viewModelScope.launch {
            try {
                val response = repository.getMovieDetailsFromApi(movieId)
                _movie.value = response
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }
}