package com.example.training_project.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.model.MovieCategory
import com.example.training_project.domain.model.MovieTab
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.ui.base.LoadingType
import com.example.training_project.utils.Resource
import com.example.training_project.utils.ResourceProvider
import kotlinx.coroutines.launch

class HomeViewModel(resourceProvider: ResourceProvider,private val useCases: MovieUseCases) : BaseViewModel(resourceProvider) {
    var currentTab = MovieTab.NOW_PLAYING
        private set
    var currentPage = 1
        private set
    var canLoadMore = true
        private set
    var isPaginating = false
        private set

    val trendingMovies = MutableLiveData<Resource<List<Movie>>>()
    val tabMovies = MutableLiveData<Resource<List<Movie>>>()
    val isRefreshing = MutableLiveData<Boolean>()

    init {
        refreshData()
    }

    fun switchTab(tab: MovieTab) {
        if (currentTab == tab) return
        currentTab = tab
        currentPage = 1
        canLoadMore = true
        fetchMovies()
    }

    fun loadNextPage() {
        if (!canLoadMore || isPaginating) return
        isPaginating= true
        currentPage++
        fetchMovies()
    }

    fun refreshData() {
        currentPage = 1
        canLoadMore = true
        isRefreshing.value = true
        fetchTrendingMovies()
        fetchMovies()
    }

    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            val cached = useCases.getCachedMovies(MovieCategory.TRENDING)

            if (cached.isNotEmpty()) {
                trendingMovies.postValue(Resource.Success(cached))
            } else {
                trendingMovies.postValue(Resource.Loading)
            }
        }
        executeApi(trendingMovies, LoadingType.SHIMMER) {
            useCases.refreshMovies(MovieCategory.TRENDING)
        }
    }

    private fun fetchMovies() {
        val isFirstPage = currentPage == 1
        executeApi(
            tabMovies,
            if (isFirstPage) LoadingType.SHIMMER else LoadingType.NONE,
            {
                isPaginating = false;
                isRefreshing.postValue(false)
            }
        ){
            val movies = useCases.refreshMovies(currentTab.category, currentPage)

            val finalList = if (isFirstPage) { movies } else {
                val current = (tabMovies.value as? Resource.Success)?.data ?: emptyList()
                (current + movies).distinctBy { it.id }
            }
            if (movies.isEmpty()) canLoadMore = false

            finalList
        }
    }
}
