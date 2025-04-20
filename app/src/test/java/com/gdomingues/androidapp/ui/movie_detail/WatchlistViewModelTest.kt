package com.gdomingues.androidapp.ui.movie_detail

import app.cash.turbine.test
import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import com.gdomingues.androidapp.domain.GetWatchlistUseCase
import com.gdomingues.androidapp.domain.ToggleWatchlistUseCase
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class WatchlistViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: WatchlistRepository = mock()
    private val toggleUseCase: ToggleWatchlistUseCase = mock()
    private val getWatchlistUseCase: GetWatchlistUseCase = mock()

    private lateinit var viewModel: WatchlistViewModel

    private val domainMovie = TrendingMovie(
        id = 101,
        title = "Inception",
        overview = "A thief enters dreams",
        backdropPath = "/inception.jpg",
        voteAverage = 8.8,
        releaseDate = LocalDate.of(2010, 7, 16)
    )

    private val uiMovie = TrendingMovieUiModel(
        id = 101,
        title = "Inception",
        overview = "A thief enters dreams",
        backdropPath = "/inception.jpg",
        voteAverage = 8.8,
        releaseDate = LocalDate.of(2010, 7, 16)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(getWatchlistUseCase()).thenReturn(flowOf(listOf(domainMovie)))
        viewModel = WatchlistViewModel(getWatchlistUseCase, toggleUseCase, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `isInWatchlist returns true if movie is in watchlist`() = runTest {
        // Given
        whenever(repository.isInWatchlist(uiMovie.id)).thenReturn(true)

        // When
        val result = viewModel.isInWatchlist(uiMovie.id)

        // Then
        assertTrue(result)
        verify(repository).isInWatchlist(uiMovie.id)
    }

    @Test
    fun `isInWatchlist returns false if movie is not in watchlist`() = runTest {
        // Given
        whenever(repository.isInWatchlist(uiMovie.id)).thenReturn(false)

        // When
        val result = viewModel.isInWatchlist(uiMovie.id)

        // Then
        assertFalse(result)
        verify(repository).isInWatchlist(uiMovie.id)
    }

    @Test
    fun `toggle calls use case and returns result`() = runTest {
        // Given
        whenever(toggleUseCase.invoke(uiMovie)).thenReturn(true)

        // When
        val result = viewModel.toggle(uiMovie)

        // Then
        assertTrue(result)
        verify(toggleUseCase).invoke(uiMovie)
    }

    @Test
    fun `watchlist emits value from use case`() = runTest {
        // Given
        val expected = listOf(domainMovie)

        // When / Then
        viewModel.watchlist.test {
            val result = awaitItem()
            assertEquals(expected, result)
            cancelAndIgnoreRemainingEvents()
        }

        verify(getWatchlistUseCase).invoke()
    }
}
