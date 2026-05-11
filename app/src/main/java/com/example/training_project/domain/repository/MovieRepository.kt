package com.example.training_project.domain.repository

import com.example.training_project.domain.model.Movie

interface MovieRepository {

    suspend fun getTrendingMovies(): List<Movie>

    suspend fun getPopularMovies(page: Int): List<Movie>

    suspend fun getTopRatedMovies(page: Int): List<Movie>

    suspend fun getUpcomingMovies(page: Int): List<Movie>

    suspend fun getNowPlayingMovies(page: Int): List<Movie>

    suspend fun getMovieDetails(movieId: Long): Movie

    suspend fun searchMovies(query: String): List<Movie>
}