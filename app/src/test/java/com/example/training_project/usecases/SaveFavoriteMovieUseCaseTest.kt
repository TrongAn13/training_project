package com.example.training_project.usecases

import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.repository.MovieRepository
import com.example.training_project.domain.usecase.SaveFavoriteMovieUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SaveFavoriteMovieUseCaseTest {
    private lateinit var repository: MovieRepository
    private lateinit var useCase: SaveFavoriteMovieUseCase
    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = SaveFavoriteMovieUseCase(repository)
    }
    @Test
    fun `invoke should call saveFavoriteMovie from repository`() = runTest {
        val movie = Movie(
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
        coEvery { repository.saveFavoriteMovie(movie) } returns Unit
        useCase(movie)
        coVerify (exactly = 1) {
            repository.saveFavoriteMovie(movie)
        }
    }
}