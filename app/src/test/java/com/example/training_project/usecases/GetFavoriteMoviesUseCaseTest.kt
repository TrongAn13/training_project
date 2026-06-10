package com.example.training_project.usecases

import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import com.example.domain.usecase.GetFavoriteMoviesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.BeforeEach

class GetFavoriteMoviesUseCaseTest {
    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetFavoriteMoviesUseCase

    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = GetFavoriteMoviesUseCase(repository)
    }
    @Test
    fun `invoke should call getFavoriteMovies from repository`() = runTest {
        val fakeMovie = listOf(
            Movie(
                id = 1,
                title = "Movie 1",
                posterUrl = "poster1.jpg",
                rating = 8.5,
                releaseDate = "2023-01-01",
                runtime = 120,
                genres = "Action, Adventure",
                backdropUrl = "backdrop1.jpg",
                overview = "Overview 1",
            ),
            Movie(
                id = 2,
                title = "Movie 2",
                posterUrl = "poster2.jpg",
                rating = 9.5,
                releaseDate = "2023-02-01",
                runtime = 1,
                genres = "Drama, Comedy",
                backdropUrl = "backdrop2.jpg",
                overview = "Overview 2",
            ),
        )
        coEvery { repository.getFavoriteMovies() } returns fakeMovie

        val result = useCase()
        assertEquals(fakeMovie, result)
        assertEquals(2, result.size)

        coVerify ( exactly = 1 ) {
            repository.getFavoriteMovies()
        }
    }
}