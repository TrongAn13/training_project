package com.example.training_project.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.model.MovieCategory
import com.example.domain.model.MovieTab
import com.example.domain.usecase.MovieUseCases
import com.example.ui.R
import com.example.ui.base.BaseViewModel
import com.example.ui.base.LoadingType
import com.example.ui.Resource
import com.example.ui.ResourceProvider
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
            }
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
