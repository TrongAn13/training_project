package com.example.training_project.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.usecase.MovieUseCases
import com.example.ui.base.BaseViewModel
import com.example.ui.base.LoadingType
import com.example.ui.Resource
import com.example.ui.ResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SearchViewModel(resourceProvider: ResourceProvider, private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    val searchResults = MutableLiveData<Resource<List<Movie>>>()
    val isEmpty = MutableLiveData<Boolean>()
    val searchHistory = MutableLiveData<Resource<List<Movie>>>()
    private var searchQuery = MutableStateFlow("")

    var currentQuery: String = ""
        private set

    init {
        viewModelScope.launch { searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    val keyword = query.trim()

                    if (keyword.isEmpty()) {
                        searchResults.value = Resource.Success(emptyList())
                        isEmpty.value = false
                        getSearchHistory()
                        return@collectLatest
                    }

                    executeApiSuspend(searchResults, LoadingType.NONE){
                        val results = useCases.searchMovies(keyword)
                        isEmpty.postValue(results.isEmpty())
                        results
                    }
                }
        }
    }
    fun searchMovies(query: String) {
        currentQuery= query.trim()
        searchQuery.value = query.trim()
    }
    fun getSearchHistory() {
        executeApi(searchHistory, LoadingType.NONE) {
            useCases.getSearchHistory()
        }
    }
    fun clearSearchHistory() {
        viewModelScope.launch {
            useCases.clearSearchHistory()
            getSearchHistory()
        }
    }
    fun saveSearchHistory(movie: Movie) {
        viewModelScope.launch {
            useCases.saveSearchHistory(movie)
        }
    }
}
