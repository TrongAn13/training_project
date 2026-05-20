package com.example.training_project.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.utils.Resource
import com.example.training_project.utils.ResourceProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(resourceProvider: ResourceProvider, private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    val searchResults = MutableLiveData<Resource<List<Movie>>>()
    val isEmpty = MutableLiveData<Boolean>()

    private var searchJob: Job? = null
    fun searchMovies(query: String) {
        searchJob?.cancel()

        if (query.length < 3) {
            searchResults.value = Resource.Success(emptyList())
            isEmpty.value = false
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            executeApi(searchResults) {
                val results = useCases.searchMovies(query)
                isEmpty.postValue(results.isEmpty())
                results
            }
        }
    }
}
