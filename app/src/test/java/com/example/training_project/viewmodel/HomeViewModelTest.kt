package com.example.training_project.viewmodel

import com.example.training_project.InstantExecutorExtension
import com.example.training_project.MainDispatcherExtension
import com.example.training_project.domain.model.Movie
import com.example.training_project.domain.model.MovieCategory
import com.example.training_project.domain.model.MovieTab
import com.example.training_project.domain.usecase.GetCachedMoviesUseCase
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.domain.usecase.RefreshMoviesUseCase
import com.example.training_project.ui.home.HomeViewModel
import com.example.training_project.utils.ResourceProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class)

class HomeViewModelTest {
    companion object {
        @JvmField
        @RegisterExtension
        val mainDispatcherExtension = MainDispatcherExtension()
    }
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var useCases: MovieUseCases
    private lateinit var getCachedMoviesUseCase: GetCachedMoviesUseCase
    private lateinit var refreshMoviesUseCase: RefreshMoviesUseCase
    private lateinit var viewModel: HomeViewModel

    private val fakeTrendingMovies = listOf(
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
    private val fakeNowPlayingMovies = listOf(
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
        getCachedMoviesUseCase = mockk()
        refreshMoviesUseCase = mockk()

        coEvery { getCachedMoviesUseCase(MovieCategory.TRENDING) } returns emptyList()

        coEvery { refreshMoviesUseCase(MovieCategory.TRENDING) } returns fakeTrendingMovies

        coEvery { refreshMoviesUseCase(MovieCategory.NOW_PLAYING) } returns fakeNowPlayingMovies
        useCases = MovieUseCases(
            mockk(),
            mockk(),
            mockk(),
            getCachedMoviesUseCase,
            refreshMoviesUseCase,
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk(),
            mockk()
        )
        viewModel = HomeViewModel(resourceProvider, useCases)
    }
    @Test
    fun init_shouldFetchTrendingMoviesandPopularMovies() = runTest {
        advanceUntilIdle()

        coVerify(exactly = 1) {
            getCachedMoviesUseCase(MovieCategory.TRENDING)
            refreshMoviesUseCase(MovieCategory.TRENDING)
            refreshMoviesUseCase(MovieCategory.NOW_PLAYING)
        }
        assertEquals(MovieTab.NOW_PLAYING, viewModel.currentTab)
        assertEquals(1, viewModel.currentPage)
        assertTrue(viewModel.canLoadMore)
        assertFalse(viewModel.isPaginating)

    }
    @Test
    fun switchTab_whenDifferentTab_shouldResetPageAndFetchMovies() = runTest {
        advanceUntilIdle()

        coEvery {
            refreshMoviesUseCase(MovieTab.NOW_PLAYING.category, 1)
        } returns fakeNowPlayingMovies

        viewModel.switchTab(MovieTab.NOW_PLAYING)
        advanceUntilIdle()

        assertEquals(MovieTab.NOW_PLAYING, viewModel.currentTab)
        assertEquals(1, viewModel.currentPage)
        assertTrue(viewModel.canLoadMore)

        coVerify(exactly = 1) {
            refreshMoviesUseCase(MovieTab.NOW_PLAYING.category, 1)
        }
    }

    @Test
    fun loadNextPage_shouldIncreaseCurrentPageAndFetchNextPage() = runTest {
        advanceUntilIdle()

        val pageTwoMovies = listOf(
            Movie(
                id = 2,
                title = "Movie 2",
                posterUrl = "poster2.jpg",
                rating = 8.0,
                releaseDate = "2023-01-02",
                runtime = 120,
                genres = "Action, Adventure",
                backdropUrl = "backdrop2.jpg",
                overview = "Overview 2",
            )
        )

        coEvery {
            refreshMoviesUseCase(MovieTab.NOW_PLAYING.category, 2)
        } returns pageTwoMovies

        viewModel.loadNextPage()
        advanceUntilIdle()

        assertEquals(2, viewModel.currentPage)
        assertFalse(viewModel.isPaginating)

        coVerify(exactly = 1) {
            refreshMoviesUseCase(MovieTab.NOW_PLAYING.category, 2)
        }
    }
}