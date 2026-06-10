package com.example.training_project.repository

import com.example.training_project.data.local.dao.FavoriteMovieDAO
import com.example.training_project.data.local.dao.MovieDAO
import com.example.training_project.data.local.dao.SearchHistoryDAO
import com.example.training_project.data.local.entity.FavoriteMovieEntity
import com.example.training_project.data.local.entity.SearchHistoryEntity
import com.example.training_project.data.network.TmdbApi
import com.example.training_project.data.repository.MovieRepositoryImpl
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class FavoriteRepositoryTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var searchDao: SearchHistoryDAO
    private lateinit var apiService: TmdbApi

    private lateinit var favoriteMovieDao: FavoriteMovieDAO
    private lateinit var movieDao: MovieDAO

    private val fakeMovies = Movie(
        id = 1,
        title = "Movie 1",
        posterUrl = "poster1.jpg",
        rating = 8.5,
        releaseDate = "2023-01-01",
        runtime = 120,
        genres = "Action, Adventure",
        backdropUrl = "backdrop1.jpg",
        overview = "Overview 1",
    )
    private val fakeSearchMovieEntity = listOf(
        FavoriteMovieEntity(
            id = 1,
            title = "Movie 1",
            posterPath = "poster1.jpg",
            voteAverage = 8.5,
            releaseDate = "2023-01-01",
            genres = "Action, Adventure",
            overview = "Overview 1",
            saveAt = System.currentTimeMillis(),
            detailViewCount = 3
        )
    )

    @Before
    fun setup() {
        searchDao = mockk()
        apiService = mockk()
        movieDao = mockk()
        favoriteMovieDao = mockk()

        movieRepository = MovieRepositoryImpl(apiService, movieDao, searchDao, favoriteMovieDao)
    }
    @Test
    fun getFavoriteMovies_ShouldReturnFromDao() = runTest{
        coEvery { favoriteMovieDao.getFavoriteMovies() } returns fakeSearchMovieEntity
        val result = movieRepository.getFavoriteMovies()
        assertEquals(1, result.size)
        assertEquals("", result[0].title)
        coVerify(exactly = 1) { favoriteMovieDao.getFavoriteMovies()}
    }
    @Test
    fun saveFavoriteMovie_ShouldSaveToDao() = runTest{
        coEvery { favoriteMovieDao.saveMovies(any()) } returns Unit
        movieRepository.saveFavoriteMovie(fakeMovies)
        coVerify(exactly = 1) {favoriteMovieDao.saveMovies(any()) }
    }
    @Test
    fun deleteFavoriteMovie_ShouldDeleteFromDao() = runTest{
        val movieId = 1L
        coEvery { favoriteMovieDao.deleteMovie(movieId) } returns Unit
        movieRepository.deleteFavoriteMovie(movieId)
        coVerify(exactly = 1) { favoriteMovieDao.deleteMovie(movieId) }
    }
    @Test
    fun isMovieSaved_ShouldReturnFromDao() = runTest{
        val movieId = 1L
        coEvery { favoriteMovieDao.isMovieSaved(movieId) } returns true
        val result = movieRepository.isMovieSaved(movieId)
        assertEquals(true, result)
        coVerify(exactly = 1) { favoriteMovieDao.isMovieSaved(movieId) }
    }
    @Test
    fun increaseDetailViewCount_ShouldIncreaseDetailViewCountInDao() = runTest{
        val movieId = 1L
        coEvery { favoriteMovieDao.increaseDetailViewCount(movieId) } returns Unit
        movieRepository.increaseDetailViewCount(movieId)
        coVerify(exactly = 1) { favoriteMovieDao.increaseDetailViewCount(movieId) }
    }
}