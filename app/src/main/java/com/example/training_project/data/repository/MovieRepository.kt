package com.example.training_project.data.repository

import com.example.training_project.data.network.RetrofitClients

class MovieRepository {
    suspend fun searchMoviesFromApi(query: String) = RetrofitClients.instance.searchMovies(query = query)
    suspend fun getTrendingMoviesFromApi() = RetrofitClients.instance.getTrendingMovies()
    suspend fun getNowPlayingMoviesFromApi(page: Int) = RetrofitClients.instance.getNowPlayingMovies(page=page)
    suspend fun getUpcomingMoviesFromApi(page: Int) = RetrofitClients.instance.getUpcomingMovies(page=page)
    suspend fun getTopRatedMoviesFromApi(page: Int) = RetrofitClients.instance.getTopRatedMovies(page=page)
    suspend fun getPopularMoviesFromApi(page: Int) = RetrofitClients.instance.getPopularMovies(page=page)

    suspend fun getMovieDetailsFromApi(movieId: Long) = RetrofitClients.instance.getMovieDetails(movieId = movieId)
}