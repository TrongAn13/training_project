package com.example.training_project.viewmodel

import com.example.training_project.InstantExecutorExtension
import com.example.training_project.MainDispatcherExtension
import com.example.domain.model.Movie
import com.example.domain.usecase.GetSearchHistoryUseCase
import com.example.domain.usecase.MovieUseCases
import com.example.domain.usecase.SaveSearchHistoryUseCase
import com.example.domain.usecase.SearchMoviesUseCase
import com.example.training_project.ui.search.SearchViewModel
import com.example.uicompose.ResourceProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@ExtendWith(
    InstantExecutorExtension::class,
    MainDispatcherExtension::class
)
class SearchViewModelTest {
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var useCases: MovieUseCases
    private lateinit var viewModel: SearchViewModel
    private lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase
    private lateinit var saveSearchHistoryUseCase: SaveSearchHistoryUseCase

    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    private val fakeMovies = listOf(
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
        )
    )
    @BeforeEach
    fun setup(){
        resourceProvider = mockk()
        getSearchHistoryUseCase = mockk()
        saveSearchHistoryUseCase = mockk()
        searchMoviesUseCase = mockk()

        coEvery { getSearchHistoryUseCase() } returns fakeMovies
        coEvery { saveSearchHistoryUseCase(any()) } returns Unit
        coEvery { searchMoviesUseCase(any()) } returns fakeMovies

        useCases = MovieUseCases(
            mockk(),
            searchMoviesUseCase,
            mockk(),
            mockk(),
            mockk(),
            getSearchHistoryUseCase,
            saveSearchHistoryUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        viewModel = SearchViewModel(resourceProvider, useCases)
    }
    @Test
    fun searchMovies_whenQueryValid_shouldUpdateCurrentQueryAndCallSearchUseCase() = runTest {
        viewModel.searchMovies("Avatar")

        advanceTimeBy(600)
        advanceUntilIdle()

        assertEquals("Avatar", viewModel.currentQuery)

        coVerify(exactly = 1) {
            searchMoviesUseCase("Avatar")
        }
    }

    @Test
    fun saveSearchHistory_whenMovieSelected_shouldCallSaveSearchHistoryUseCase() = runTest {
        val movie = fakeMovies.first()

        viewModel.saveSearchHistory(movie)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            saveSearchHistoryUseCase(movie)
        }
    }
}