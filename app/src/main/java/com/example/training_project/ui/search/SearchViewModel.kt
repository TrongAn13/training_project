package com.example.training_project.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class SearchViewModel : ViewModel(){
    private val repository = MovieRepository()
    private var searchJob: Job? = null

    val searchResults = MutableLiveData<List<Movie>>()
    val isEmpty = MutableLiveData<Boolean>()

    fun searchMovies(query: String) {

        searchJob?.cancel()

        if (query.isEmpty()) {
            searchResults.value = emptyList()
            isEmpty.value = false
            return
        }

        searchJob = viewModelScope.launch {
            try {
                val response = repository.searchMoviesFromApi(query)
                val results = response.results ?: emptyList()

                searchResults.value = results
                isEmpty.value = results.isEmpty()

            } catch (e: Exception) {
                if (e is CancellationException) throw e

                searchResults.value = emptyList()
                isEmpty.value = true
            }
        }
    }
}