package com.example.training_project.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.model.MovieCategory
import com.example.training_project.domain.model.MovieTab
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.utils.Resource
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            val cached = useCases.getCachedMovies(MovieCategory.TRENDING)

            if (cached.isNotEmpty()) {
                trendingMovies.postValue(Resource.Success(cached))
            } else {
                trendingMovies.postValue(Resource.Loading)
            }

            try {
                val fresh = useCases.refreshMovies(MovieCategory.TRENDING)
                trendingMovies.postValue(Resource.Success(fresh))
            } catch (e: Exception) {
                if (trendingMovies.value !is Resource.Success) {
                    trendingMovies.postValue(Resource.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun fetchMovies() {
        val isFirstPage = currentPage == 1
        viewModelScope.launch {
            if (isFirstPage) {
                val cached = useCases.getCachedMovies(currentTab.category)

                if (cached.isNotEmpty()) {
                    tabMovies.postValue(Resource.Success(cached))
                } else {
                    tabMovies.postValue(Resource.Loading)
                }
            }

            try {
                val fresh = useCases.refreshMovies(currentTab.category, currentPage)
                val finalList = if (isFirstPage) {
                    fresh
                } else {
                    val current = (tabMovies.value as? Resource.Success)?.data ?: emptyList()
                    current + fresh
                }

                if (fresh.isEmpty()) canLoadMore = false

                tabMovies.postValue(Resource.Success(finalList))
            } catch (e: Exception) {
                if (tabMovies.value !is Resource.Success) {
                    tabMovies.postValue(Resource.Error(e.message ?: "Unknown error"))
                }
            } finally {
                isRefreshing.postValue(false)
            }
        }
    }
}
