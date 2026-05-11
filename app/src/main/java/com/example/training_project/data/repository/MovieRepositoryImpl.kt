package com.example.training_project.data.repository

import android.content.Context
import com.example.training_project.data.local.AppDatabase
import com.example.training_project.data.local.dao.MovieDAO
import com.example.training_project.data.mapper.MovieMapper.toDomain
import com.example.training_project.data.mapper.MovieMapper.toEntity
import com.example.training_project.data.network.RetrofitClients
import com.example.training_project.data.network.TmdbApi
import com.example.training_project.data.remote.DTO.MovieDTO
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(private val apiService: TmdbApi, private val movieDao: MovieDAO) : MovieRepository {
    
    companion object {
        @Volatile
        private var INSTANCE: MovieRepositoryImpl? = null

        fun getInstance(context: Context): MovieRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getDatabase(context)
                val instance = MovieRepositoryImpl(RetrofitClients.instance, database.movieDao())
                INSTANCE = instance
                instance
            }
        }
    }

    private suspend fun getMoviesInternal(
        category: String,
        page: Int = 1,
        fetchFromRemote: suspend () -> List<MovieDTO>
    ): List<Movie> = withContext(Dispatchers.IO) {
        try {
            val dtos = fetchFromRemote()
            if (dtos.isNotEmpty()) {
                val entities = dtos.map { it.toEntity(category) }
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

    override suspend fun getTrendingMovies() = getMoviesInternal("trending") {
        apiService.getTrendingMovies().results ?: emptyList()
    }

    override suspend fun getPopularMovies(page: Int) = getMoviesInternal("popular", page) {
        apiService.getPopularMovies(page = page).results ?: emptyList()
    }

    override suspend fun getTopRatedMovies(page: Int) = getMoviesInternal("top_rated", page) {
        apiService.getTopRatedMovies(page = page).results ?: emptyList()
    }

    override suspend fun getNowPlayingMovies(page: Int) = getMoviesInternal("now_playing", page) {
        apiService.getNowPlayingMovies(page = page).results ?: emptyList()
    }

    override suspend fun getUpcomingMovies(page: Int) = getMoviesInternal("upcoming", page) {
        apiService.getUpcomingMovies(page = page).results ?: emptyList()
    }

    override suspend fun searchMovies(query: String) = withContext(Dispatchers.IO) {
        apiService.searchMovies(query = query).results?.map { it.toDomain() } ?: emptyList()
    }

    override suspend fun getMovieDetails(movieId: Long): Movie = withContext(Dispatchers.IO) {
        apiService.getMovieDetails(movieId = movieId).toDomain()
    }
}