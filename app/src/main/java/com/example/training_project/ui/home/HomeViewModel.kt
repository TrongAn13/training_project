package com.example.training_project.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

enum class MovieTab {
    NOW_PLAYING, UPCOMING, TOP_RATED, POPULAR
}
class HomeViewModel: ViewModel() {
    private val repository = MovieRepository()
    var currentTab = MovieTab.NOW_PLAYING
        private set
    var currentPage = 1
        private set
    var isLoading = false
        private set
    val trendingMovies = MutableLiveData<List<Movie>>()
    val tabMovies = MutableLiveData<List<Movie>>()
    val isRefreshing = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    init {
        fetchTrendingMovies()
        fetchMovies()
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
        viewModelScope.launch {
            try {
                val response = repository.getTrendingMoviesFromApi()
                trendingMovies.value = response.results ?: emptyList()

            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }
    private fun fetchMovies(){
        isLoading = true
        if (currentPage == 1 && isRefreshing.value != true) {
            isRefreshing.value = true
        }

        viewModelScope.launch {
            try {
                val response = when (currentTab) {
                    MovieTab.NOW_PLAYING -> repository.getNowPlayingMoviesFromApi(currentPage)
                    MovieTab.UPCOMING -> repository.getUpcomingMoviesFromApi(currentPage)
                    MovieTab.TOP_RATED -> repository.getTopRatedMoviesFromApi(currentPage)
                    MovieTab.POPULAR -> repository.getPopularMoviesFromApi(currentPage)
                }

                val newResults = response.results ?: emptyList()

                if (currentPage == 1) {
                    tabMovies.value = newResults
                } else {
                    val currentList = tabMovies.value.orEmpty().toMutableList()
                    currentList.addAll(newResults)
                    tabMovies.value = currentList
                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    errorMessage.value = e.message
                }
            } finally {
                isLoading = false
                isRefreshing.value = false
            }
        }
    }
}