package com.gdomingues.androidapp.ui.trending_movies

import app.cash.turbine.test
import com.gdomingues.androidapp.domain.GetTrendingMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingMoviesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase = mock()

    private val sampleUiModels = listOf(
        TrendingMovieUiModel(
            id = 1,
            title = "Movie A",
            overview = "Overview A",
            backdropPath = "https://image.tmdb.org/t/p/original/ce3prrjh9ZehEl5JinNqr4jIeaB.jpg",
            voteAverage = 7.8
        ),
        TrendingMovieUiModel(
            id = 2,
            title = "Movie B",
            overview = "Overview B",
            backdropPath = "https://image.tmdb.org/t/p/original/ce3prrjh9ZehEl5JinNqr4jIeaB.jpg",
            voteAverage = 8.2
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emits Loading then Success when use case returns data`() = testScope.runTest {
        whenever(getTrendingMoviesUseCase()).thenReturn(sampleUiModels)

        val viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)

        viewModel.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(TrendingMoviesUiState.Success(sampleUiModels), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Loading then Error when use case throws exception`() = testScope.runTest {
        whenever(getTrendingMoviesUseCase()).thenThrow(RuntimeException("API failure"))

        val viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)

        viewModel.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            advanceUntilIdle()
            val error = awaitItem()
            assertTrue(error is TrendingMoviesUiState.Error)
            assertTrue(error.message.contains("API failure"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchMovies triggers data reload`() = testScope.runTest {
        whenever(getTrendingMoviesUseCase()).thenReturn(sampleUiModels)

        val viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)

        viewModel.fetchMovies()

        viewModel.uiState.test {
            assertEquals(TrendingMoviesUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(TrendingMoviesUiState.Success(sampleUiModels), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(getTrendingMoviesUseCase, times(2)).invoke() // init + manual retry
    }
}
