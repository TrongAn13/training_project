package com.example.training_project.repository

import com.example.training_project.data.local.dao.FavoriteMovieDAO
import com.example.training_project.data.local.dao.MovieDAO
import com.example.training_project.data.local.dao.SearchHistoryDAO
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

class SearchRepositoryTest {
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
        SearchHistoryEntity(
        id = 1,
        title = "Movie 1",
        posterPath = "poster1.jpg",
        voteAverage = 8.5,
        releaseDate = "2023-01-01",
        genres = "Action, Adventure",
        overview = "Overview 1",
        saveAt = System.currentTimeMillis()
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
    fun getSearchHistory_ShouldReturnFromDao() = runTest{
        coEvery { searchDao.getSearchHistory() } returns fakeSearchMovieEntity
        val result = movieRepository.getSearchHistory()
        assertEquals(1, result.size)
        assertEquals("avatar", result[0].title)
        coVerify(exactly = 1) { searchDao.getSearchHistory()}
    }
    @Test
    fun saveSearchHistory_ShouldSaveToDao() = runTest{
        coEvery { searchDao.insertHistory(any()) } returns Unit
        movieRepository.saveSearchHistory(fakeMovies)
        coVerify(exactly = 1) { searchDao.insertHistory(any()) }
    }
}