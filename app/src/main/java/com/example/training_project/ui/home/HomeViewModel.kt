package com.example.training_project.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.utils.Resource
import com.example.training_project.ui.base.LoadingType

enum class MovieTab {
    NOW_PLAYING, UPCOMING, TOP_RATED, POPULAR
}

class HomeViewModel(application: Application,private val useCases: MovieUseCases) : BaseViewModel(application) {
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
        executeApi(trendingMovies) {
            useCases.getMovies("trending")
        }
    }

    private fun fetchMovies() {
        val isFirstPage = currentPage == 1
        val loadingType = if (isFirstPage) LoadingType.SHIMMER else LoadingType.NONE

        executeApi(tabMovies, loadingType) {
            val results = when (currentTab) {
                MovieTab.NOW_PLAYING -> useCases.getMovies("nowplaying", currentPage)
                MovieTab.UPCOMING -> useCases.getMovies("up_coming", currentPage)
                MovieTab.TOP_RATED -> useCases.getMovies("top_rated", currentPage)
                MovieTab.POPULAR -> useCases.getMovies("popular", currentPage)
            }

            if (results.isEmpty()) canLoadMore = false

            if (isFirstPage) {
                isRefreshing.postValue(false)
                results
            } else {
                val current = (tabMovies.value as? Resource.Success)?.data ?: emptyList()
                current + results
            }
        }
    }
}
