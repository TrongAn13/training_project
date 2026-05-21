package com.example.training_project.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.utils.Resource
import com.example.training_project.utils.ResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class SearchViewModel(resourceProvider: ResourceProvider, private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    val searchResults = MutableLiveData<Resource<List<Movie>>>()
    val isEmpty = MutableLiveData<Boolean>()

    private var searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery.debounce(500)
                .distinctUntilChanged()
                .filter { it.length >= 3 }
                .collectLatest { query ->
                    executeApi(searchResults) {
                        val results = useCases.searchMovies(query)
                        isEmpty.postValue(results.isEmpty())
                        results
                    }
                }
        }
    }
    fun searchMovies(query: String) {
        if (query.length < 3) {
            searchResults.value = Resource.Success(emptyList())
            isEmpty.value = false
        }
        searchQuery.value = query.trim()
    }
}
