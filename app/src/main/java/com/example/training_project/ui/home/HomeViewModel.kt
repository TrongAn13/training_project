package com.example.training_project.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training_project.data.model.Movie
import com.example.training_project.data.repository.MovieRepository
import com.example.training_project.utils.Resource
import com.example.training_project.utils.executeApi
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
            val response = repository.getTrendingMoviesFromApi()
            response.results ?: emptyList()
        }
    }
    private fun fetchMovies(){
        isLoading = true
        if (currentPage == 1 && isRefreshing.value != true) {
            tabMovies.value = Resource.Loading
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
                    tabMovies.value = Resource.Success(newResults)
                } else {
                    val currentResource = tabMovies.value
                    val currentList = if (currentResource is Resource.Success) currentResource.data.toMutableList() else mutableListOf()
                    currentList.addAll(newResults)
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