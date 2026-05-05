package com.example.training_project.data.repository

import com.example.training_project.data.network.RetrofitClients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository {
    suspend fun searchMoviesFromApi(query: String) = withContext(Dispatchers.IO) {
        RetrofitClients.instance.searchMovies(query = query)
    }
    suspend fun getTrendingMoviesFromApi() = withContext(Dispatchers.IO) {
        RetrofitClients.instance.getTrendingMovies()
    }
    suspend fun getNowPlayingMoviesFromApi(page: Int) = withContext(Dispatchers.IO) {
        RetrofitClients.instance.getNowPlayingMovies(page=page)
    }
    suspend fun getUpcomingMoviesFromApi(page: Int) = withContext(Dispatchers.IO) {
        RetrofitClients.instance.getUpcomingMovies(page=page)
    }
    suspend fun getTopRatedMoviesFromApi(page: Int) = withContext(Dispatchers.IO) {
        RetrofitClients.instance.getTopRatedMovies(page=page)
    }
    suspend fun getPopularMoviesFromApi(page: Int) = withContext(Dispatchers.IO) {
        RetrofitClients.instance.getPopularMovies(page=page)
    }
    suspend fun getMovieDetailsFromApi(movieId: Long) = withContext(Dispatchers.IO) {
        RetrofitClients.instance.getMovieDetails(movieId = movieId)
    }
}