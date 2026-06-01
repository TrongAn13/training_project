package com.example.data.repository

import com.example.database.dao.FavoriteMovieDAO
import com.example.database.dao.MovieDAO
import com.example.database.dao.SearchHistoryDAO
import com.example.data.mapper.MovieMapper.toDomain
import com.example.data.mapper.MovieMapper.toEntity
import com.example.data.mapper.MovieMapper.toFavoriteEntity
import com.example.data.mapper.MovieMapper.toSearchHistoryEntity
import com.example.network.dto.MovieDTO
import com.example.domain.model.Movie
import com.example.domain.model.MovieCategory
import com.example.domain.repository.MovieRepository
import com.example.network.network.TmdbApi.TmdbApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val apiService: TmdbApi,
    private val movieDao: MovieDAO,
    private val searchHistoryDao: SearchHistoryDAO,
    private val favoriteMovieDao: FavoriteMovieDAO
) : MovieRepository {
    private suspend fun getMoviesInternal(
        category: String,
        page: Int = 1,
        fetchFromRemote: suspend () -> List<MovieDTO>
    ): List<Movie> = withContext(Dispatchers.IO) {
        try {
            val dtos = fetchFromRemote()
            if (dtos.isNotEmpty()) {
                val entities = dtos.mapIndexed { index, dto ->
                    dto.toEntity(
                        category = category,
                        page = page,
                        position = index
                    )
                }
                if (page == 1) {
                    movieDao.deleteMoviesByCategory(category)
                }
                movieDao.insertMovies(entities)
            }
            movieDao.getMoviesByCategory(category).map { it.toDomain() }
        } catch (e: Exception) {
            val cached = movieDao.getMoviesByCategory(category)
            if (cached.isNotEmpty()) cached.map { it.toDomain() } else throw e
        }
    }

    override suspend fun getTrendingMovies() = getMoviesInternal(MovieCategory.TRENDING.value) {
        apiService.getTrendingMovies().results ?: emptyList()
    }

    override suspend fun getPopularMovies(page: Int) = getMoviesInternal(MovieCategory.POPULAR.value, page) {
        apiService.getPopularMovies(page = page).results ?: emptyList()
    }

    override suspend fun getTopRatedMovies(page: Int) = getMoviesInternal(MovieCategory.TOP_RATED.value, page) {
        apiService.getTopRatedMovies(page = page).results ?: emptyList()
    }

    override suspend fun getNowPlayingMovies(page: Int) = getMoviesInternal(MovieCategory.NOW_PLAYING.value, page) {
        apiService.getNowPlayingMovies(page = page).results ?: emptyList()
    }

    override suspend fun getUpcomingMovies(page: Int) = getMoviesInternal(MovieCategory.UPCOMING.value, page) {
        apiService.getUpcomingMovies(page = page).results ?: emptyList()
    }

    override suspend fun searchMovies(query: String) = withContext(Dispatchers.IO) {
        apiService.searchMovies(query = query).results?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getMovieDetails(movieId: Long): Movie = withContext(Dispatchers.IO) {
        apiService.getMovieDetails(movieId = movieId).toDomain()
    }

    override suspend fun getCachedMovies(
        category: MovieCategory
    ): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getMoviesByCategory(category.value)
            .map { it.toDomain() }
    }

    override suspend fun refreshMovies(category: MovieCategory, page: Int
    ): List<Movie> = withContext(Dispatchers.IO) {
        val dtos = when (category) {
            MovieCategory.NOW_PLAYING ->
                apiService.getNowPlayingMovies(page).results ?: emptyList()

            MovieCategory.POPULAR ->
                apiService.getPopularMovies(page).results ?: emptyList()

            MovieCategory.TOP_RATED ->
                apiService.getTopRatedMovies(page).results ?: emptyList()

            MovieCategory.UPCOMING ->
                apiService.getUpcomingMovies(page).results ?: emptyList()

            MovieCategory.TRENDING ->
                apiService.getTrendingMovies().results ?: emptyList()
        }
        val entities = dtos.mapIndexed { index, dto ->
            dto.toEntity(
                category = category.value,
                page = page,
                position = index
            )
        }
        if (page == 1) {
            movieDao.deleteMoviesByCategory(category.value)
        }
        movieDao.insertMovies(entities)
        movieDao.getMoviesByCategory(category.value)
            .map { it.toDomain() }
    }

    override suspend fun clearSearchHistory() {
        searchHistoryDao.deleteHistory()
    }
    override suspend fun getSearchHistory(): List<Movie> {
        return searchHistoryDao.getSearchHistory().map { it.toDomain() }
    }
    override suspend fun saveSearchHistory(movie: Movie) {
        searchHistoryDao.insertHistory(movie.toSearchHistoryEntity())
    }

    override suspend fun saveFavoriteMovie(movie: Movie) {
       favoriteMovieDao.saveMovies(movie.toFavoriteEntity())
    }
    override suspend fun getFavoriteMovies(): List<Movie> {
        return favoriteMovieDao.getFavoriteMovies().map { it.toDomain() }
    }
    override suspend fun deleteFavoriteMovie(movieId: Long) {
        favoriteMovieDao.deleteMovie(movieId)
    }
    override suspend fun isMovieSaved(movieId: Long): Boolean {
        return favoriteMovieDao.isMovieSaved(movieId)
    }
    override suspend fun increaseDetailViewCount(movieId: Long) {
        favoriteMovieDao.increaseDetailViewCount(movieId)
    }
}