package com.example.training_project.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import com.example.training_project.utils.Resource
import com.example.training_project.ui.base.LoadingType

enum class MovieTab {
    NOW_PLAYING, UPCOMING, TOP_RATED, POPULAR
}
class HomeViewModel(application: Application): BaseViewModel(application) {
    private val repository = MovieRepository()
    var currentTab = MovieTab.NOW_PLAYING
        private set
    var currentPage = 1
        private set
    var canLoadMore = true
        private set
    val trendingMovies = MutableLiveData<Resource<List<Movie>>>()
    val tabMovies = MutableLiveData<Resource<List<Movie>>>()
    val isRefreshing = MutableLiveData<Boolean>()


    init {
        fetchTrendingMovies()
        fetchMovies()
    }
    fun switchTab(tab: MovieTab) {
        if (currentTab == tab) return
        currentTab = tab
        currentPage = 1
        canLoadMore = true
        fetchMovies()
    }

    fun loadNextPage() {
        if (!canLoadMore) return
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
        trendingMovies.value = Resource.Loading
        executeApi(trendingMovies, LoadingType.SHIMMER) {
            val response = repository.getTrendingMoviesFromApi()
            response.results ?: emptyList()
        }
    }
    private fun fetchMovies() {
        val loadingType = if (currentPage == 1 && isRefreshing.value != true) {
            LoadingType.SHIMMER
        } else {
            LoadingType.NONE
        }

        executeApi(tabMovies, loadingType) {
            try {
                val response = when (currentTab) {
                    MovieTab.NOW_PLAYING -> repository.getNowPlayingMoviesFromApi(currentPage)
                    MovieTab.UPCOMING -> repository.getUpcomingMoviesFromApi(currentPage)
                    MovieTab.TOP_RATED -> repository.getTopRatedMoviesFromApi(currentPage)
                    MovieTab.POPULAR -> repository.getPopularMoviesFromApi(currentPage)
                }

                val newResults = response.results ?: emptyList()
                val totalPages = response.totalPages ?: 1
                canLoadMore = currentPage < totalPages

                if (currentPage == 1) {
                    newResults
                } else {
                    val currentResource = tabMovies.value
                    val currentList = if (currentResource is Resource.Success) currentResource.data.toMutableList() else mutableListOf()
                    currentList.addAll(newResults)
                    currentList
                }
            } finally {
                isRefreshing.postValue(false)
            }
        }
    }
}
