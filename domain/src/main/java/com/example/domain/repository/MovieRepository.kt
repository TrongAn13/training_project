package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.Movie
import com.example.domain.model.MovieCategory
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getTrendingMovies(): List<Movie>

    suspend fun getPopularMovies(page: Int): List<Movie>

    suspend fun getTopRatedMovies(page: Int): List<Movie>

    suspend fun getUpcomingMovies(page: Int): List<Movie>

    suspend fun getNowPlayingMovies(page: Int): List<Movie>

    suspend fun getMovieDetails(movieId: Long): Movie

    suspend fun searchMovies(query: String): List<Movie>

    suspend fun getCachedMovies(category: MovieCategory): List<Movie>

    suspend fun refreshMovies(category: MovieCategory, page: Int = 1): List<Movie>

    suspend fun saveSearchHistory(movie: Movie)

    suspend fun getSearchHistory(): List<Movie>

    suspend fun clearSearchHistory()

    suspend fun saveFavoriteMovie(movie: Movie)

    suspend fun getFavoriteMovies(): List<Movie>

    suspend fun deleteFavoriteMovie(movieId: Long)

    suspend fun isMovieSaved(movieId: Long): Boolean
    suspend fun increaseDetailViewCount(movieId: Long)

    fun getMoviesPaging(category: MovieCategory): Flow<PagingData<Movie>>
}