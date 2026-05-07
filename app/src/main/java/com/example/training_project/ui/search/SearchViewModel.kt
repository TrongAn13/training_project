package com.example.training_project.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import com.example.training_project.utils.Resource
import com.example.training_project.utils.executeApi
import kotlinx.coroutines.Job

class SearchViewModel : ViewModel(){
    private val repository = MovieRepository()
    val searchResults = MutableLiveData<Resource<List<Movie>>>()
    val isEmpty = MutableLiveData<Boolean>()

    fun searchMovies(query: String) {
        if (query.length < 3) {
            searchResults.value =
                Resource.Success(emptyList())
            isEmpty.value = false
            return
        }

        executeApi(searchResults) {
            val response = repository.searchMoviesFromApi(query)
            val results = response.results ?: emptyList()
            isEmpty.postValue( results.isEmpty())
            results
        }
    }
}