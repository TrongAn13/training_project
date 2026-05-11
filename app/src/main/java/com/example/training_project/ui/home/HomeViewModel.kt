package com.example.training_project.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.training_project.data.repository.MovieRepositoryImpl
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.ui.base.BaseViewModel
import com.example.training_project.utils.Resource
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

enum class MovieTab {
    NOW_PLAYING, UPCOMING, TOP_RATED, POPULAR
}

class HomeViewModel(private val useCases: MovieUseCases) : BaseViewModel() {
    var currentTab = MovieTab.NOW_PLAYING
        private set
    var currentPage = 1
        private set
    var isLoading = false
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
        fetchMovies()
    }

    fun loadNextPage() {
        if (isLoading) return
        currentPage++
        fetchMovies()
    }

    fun refreshData() {
        currentPage = 1
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
        isLoading = true
        if (currentPage == 1 && isRefreshing.value != true) {
            tabMovies.value = Resource.Loading
        }

        viewModelScope.launch {
            try {
                val results = when (currentTab) {
                    MovieTab.NOW_PLAYING -> useCases.getMovies("nowplaying", currentPage)
                    MovieTab.UPCOMING -> useCases.getMovies("up_coming",currentPage)
                    MovieTab.TOP_RATED -> useCases.getMovies("top_rated",currentPage)
                    MovieTab.POPULAR -> useCases.getMovies("popular",currentPage)
                }

                if (currentPage == 1) {
                    tabMovies.value = Resource.Success(results)
                } else {
                    val currentResource = tabMovies.value
                    val currentList = if (currentResource is Resource.Success) {
                        currentResource.data.toMutableList()
                    } else {
                        mutableListOf()
                    }
                    currentList.addAll(results)
                    tabMovies.value = Resource.Success(currentList)
                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    tabMovies.value = Resource.Error(e.message ?: "Lỗi kết nối mạng")
                }
            } finally {
                isLoading = false
                isRefreshing.value = false
            }
        }
    }
}
