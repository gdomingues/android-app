package com.gdomingues.androidapp.ui.trending_movies

import com.gdomingues.androidapp.domain.GetTrendingMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingMoviesViewModelTest {

    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TrendingMoviesViewModel

    private val firstPageMovies = (1..5).map {
        TrendingMovieUiModel(
            id = it,
            title = "Movie $it",
            overview = "Overview $it",
            backdropPath = "https://image.tmdb.org/t/p/w780/movie$it.jpg",
            voteAverage = 7.0 + it
        )
    }

    private val secondPageMovies = (6..10).map {
        TrendingMovieUiModel(
            id = it,
            title = "Movie $it",
            overview = "Overview $it",
            backdropPath = "https://image.tmdb.org/t/p/w780/movie$it.jpg",
            voteAverage = 7.0 + it
        )
    }

    private val firstPage = TrendingMoviesUiModel(
        totalPages = 2,
        movies = firstPageMovies
    )

    private val secondPage = TrendingMoviesUiModel(
        totalPages = 2,
        movies = secondPageMovies
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
    fun `Given use case returns data When viewModel is initialized Then emits Success with movies`() =
        runTest {
            // Given
            whenever(getTrendingMoviesUseCase.invoke(1)).thenReturn(firstPage)

            // When
            viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)

            // Then
            advanceUntilIdle()
            val state = viewModel.uiState.value
            assertIs<TrendingMoviesUiState.Success>(state)
            assertEquals(firstPageMovies, state.movies)
            assertEquals(false, state.isLoadingMore)
        }

    @Test
    fun `Given use case throws error When viewModel is initialized Then emits Error state`() =
        runTest {
            // Given
            whenever(getTrendingMoviesUseCase.invoke(1)).thenThrow(RuntimeException("Boom"))

            // When
            viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)

            // Then
            advanceUntilIdle()
            val state = viewModel.uiState.value
            assertIs<TrendingMoviesUiState.Error>(state)
        }

    @Test
    fun `Given initial Success and more pages When fetchMoreTrendingMovies is called Then appends new movies`() =
        runTest {
            // Given
            whenever(getTrendingMoviesUseCase.invoke(1)).thenReturn(firstPage)
            whenever(getTrendingMoviesUseCase.invoke(2)).thenReturn(secondPage)

            viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)
            advanceUntilIdle()

            // When
            viewModel.fetchMoreTrendingMovies()
            advanceUntilIdle()

            // Then
            val state = viewModel.uiState.value
            assertIs<TrendingMoviesUiState.Success>(state)
            assertEquals(10, state.movies.size)
            assertEquals("Movie 10", state.movies.last().title)
            assertEquals(false, state.isLoadingMore)
        }

    @Test
    fun `Given no more pages When fetchMoreTrendingMovies is called Then it does not update movies`() =
        runTest {
            // Given
            val singlePage = TrendingMoviesUiModel(totalPages = 1, movies = firstPageMovies)
            whenever(getTrendingMoviesUseCase.invoke(1)).thenReturn(singlePage)

            viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)
            advanceUntilIdle()

            // When
            viewModel.fetchMoreTrendingMovies()
            advanceUntilIdle()

            // Then
            val state = viewModel.uiState.value
            assertIs<TrendingMoviesUiState.Success>(state)
            assertEquals(firstPageMovies, state.movies)
            assertEquals(false, state.isLoadingMore)
        }

    @Test
    fun `Given failure during pagination When fetchMoreTrendingMovies is called Then retains old state and isLoadingMore is false`() =
        runTest {
            // Given
            whenever(getTrendingMoviesUseCase.invoke(1)).thenReturn(firstPage)
            whenever(getTrendingMoviesUseCase.invoke(2)).thenThrow(RuntimeException("Oops"))

            viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)
            advanceUntilIdle()

            // When
            viewModel.fetchMoreTrendingMovies()
            advanceUntilIdle()

            // Then
            val state = viewModel.uiState.value
            assertIs<TrendingMoviesUiState.Success>(state)
            assertEquals(firstPageMovies, state.movies)
            assertEquals(false, state.isLoadingMore)
        }

    @Test
    fun `Given error previously When fetchMovies is retried Then emits Success again if data is fetched`() =
        runTest {
            // Given
            whenever(getTrendingMoviesUseCase.invoke(1))
                .thenThrow(RuntimeException("Initial fail"))
                .thenReturn(firstPage)

            // When
            viewModel = TrendingMoviesViewModel(getTrendingMoviesUseCase)
            advanceUntilIdle()

            // Then (initial error)
            assertIs<TrendingMoviesUiState.Error>(viewModel.uiState.value)

            // When
            viewModel.fetchMovies()
            advanceUntilIdle()

            // Then
            val retriedState = viewModel.uiState.value
            assertIs<TrendingMoviesUiState.Success>(retriedState)
            assertEquals(firstPageMovies, retriedState.movies)
        }
}
