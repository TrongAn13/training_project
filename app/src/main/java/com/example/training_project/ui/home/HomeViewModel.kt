package com.example.training_project.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.Movie
import com.example.domain.model.MovieCategory
import com.example.domain.model.MovieTab
import com.example.domain.usecase.MovieUseCases
import com.example.ui.base.BaseViewModel
import com.example.ui.Resource
import com.example.ui.ResourceProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel(resourceProvider: ResourceProvider,private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    var currentTab = MovieTab.NOW_PLAYING
        private set
    val trendingMovies = MutableLiveData<Resource<List<Movie>>>()
    val isRefreshing = MutableLiveData<Boolean>()
    private val currentTabFlow = MutableStateFlow(currentTab)
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
        if (currentTab == tab) return
        currentTab = tab
        currentTabFlow.value = tab
    }
    fun refreshData() {
        isRefreshing.value = true
        fetchTrendingMovies()
    }
    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            val cached = useCases.getCachedMovies(MovieCategory.TRENDING)
            val hasCached = cached.isNotEmpty()

            if (cached.isNotEmpty()) {
                trendingMovies.postValue(Resource.Success(cached))
            } else {
                trendingMovies.postValue(Resource.Loading)
            }
            try{
                val fresh = useCases.refreshMovies(MovieCategory.TRENDING)
                trendingMovies.postValue(Resource.Success(fresh))
            }
            catch (e: Exception){
                if (hasCached) {
                    trendingMovies.postValue(Resource.Success(cached))
                } else {
                    trendingMovies.postValue(Resource.Error(getErrorMessage(e)))
                }
            }finally {
                isRefreshing.postValue(false)
            }
        }
    }
}
