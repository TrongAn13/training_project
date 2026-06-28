package com.example.training_project.ui.search

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.usecase.MovieUseCases
import com.example.uicompose.ResourceProvider
import com.example.uicompose.base.BaseComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val searchResults: List<Movie> = emptyList(),
    val searchHistory: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null
)

class SearchViewModel(resourceProvider: ResourceProvider, private val useCases: MovieUseCases) : BaseComposeViewModel(resourceProvider) {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()
    private var searchQuery = MutableStateFlow("")

    init {
        observeSearchQuery()
        loadHistory()
    }

    private fun observeSearchQuery(){
        viewModelScope.launch {
            searchQuery.debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isEmpty()) return@collectLatest

                    executeApiState(
                        onLoading = { loading ->
                            _uiState.update { it.copy(
                                isLoading = loading,
                                error = null
                            ) }
                        },
                        onSuccess = { movies ->
                            _uiState.update {
                                it.copy(
                                    searchResults = movies,
                                    isEmpty = movies.isEmpty(),
                                    error = null
                                )
                            }
                        },
                        onError = { message ->
                            _uiState.update { it.copy(
                                error = message
                            ) }
                        }
                    ) {
                        useCases.searchMovies(query)
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        val trimmedQuery = query.trim()
        _uiState.update { it.copy(query = trimmedQuery) }

        if (trimmedQuery.isEmpty()) {
            _uiState.update { it.copy(
                searchResults = emptyList(),
                isEmpty = false,
                isLoading = false,
                error = null
            ) }
            loadHistory()
        }
        
        searchQuery.value = trimmedQuery
    }

    fun loadHistory() {
        executeApiState(
            onLoading = {},
            onSuccess = { movies ->
                _uiState.update { it.copy(
                    searchHistory = movies,
                ) }
            },
            onError = { message ->
                _uiState.update { it.copy(
                    error = message
                ) }
            }
        )
        {
            useCases.getSearchHistory()
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            useCases.clearSearchHistory()
            _uiState.update { it.copy(
                searchHistory = emptyList()
            ) }
        }
    }

    fun saveSearchHistory(movie: Movie) {
        viewModelScope.launch {
            useCases.saveSearchHistory(movie)
            loadHistory()
        }
    }
}
