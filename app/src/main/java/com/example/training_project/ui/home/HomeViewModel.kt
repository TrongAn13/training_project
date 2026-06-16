package com.example.training_project.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.Movie
import com.example.domain.model.MovieCategory
import com.example.domain.model.MovieTab
import com.example.domain.usecase.MovieUseCases
import com.example.uicompose.ResourceProvider
import com.example.uicompose.base.BaseComposeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val trendingMovies: List<Movie> = emptyList(),
    val currentTab: MovieTab = MovieTab.NOW_PLAYING,
    val isRefreshing: Boolean = false,
    val isLoadingTrending: Boolean = false,
    val error: String? = null
)
class HomeViewModel(resourceProvider: ResourceProvider,private val useCases: MovieUseCases) : BaseComposeViewModel(resourceProvider) {
    val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private val currentTabFlow = MutableStateFlow(MovieTab.NOW_PLAYING)
    private val pagingCache = mutableMapOf<MovieTab, Flow<PagingData<Movie>>>()
    @OptIn(ExperimentalCoroutinesApi::class)
    val moviesPaging: Flow<PagingData<Movie>> = currentTabFlow
        .flatMapLatest { tab ->
            getPagingFlow(tab)
        }
    init {
        refreshData()
    }
    private fun getPagingFlow(tab: MovieTab) : Flow<PagingData<Movie>> {
        return pagingCache.getOrPut(tab) {
            useCases.getMoviesPaging(tab.category)
                .cachedIn(viewModelScope)
        }
    }
    fun switchTab(tab: MovieTab) {
        if(_uiState.value.currentTab == tab) return
        currentTabFlow.value = tab
        _uiState.update { it.copy(currentTab = tab) }
    }
    fun refreshData() {
        _uiState.update { it.copy(isRefreshing = true) }
        fetchTrendingMovies()
    }
    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            val cached = useCases.getCachedMovies(MovieCategory.TRENDING)
            val hasCached = cached.isNotEmpty()
            if (hasCached) {
                _uiState.update { it.copy(
                        trendingMovies = cached
                    )
                }
            }
            try {
                val fresh = useCases.refreshMovies(MovieCategory.TRENDING)
                _uiState.update { it.copy(
                        trendingMovies = fresh,
                        error = null
                    )
                }
            } catch (e: Exception) {
                if (!hasCached) {
                    _uiState.update { it.copy(
                            error = getErrorMessage(e)
                        )
                    }
                }
            } finally {
                _uiState.update { it.copy(
                        isRefreshing = false
                    )
                }
            }
        }
    }
}
