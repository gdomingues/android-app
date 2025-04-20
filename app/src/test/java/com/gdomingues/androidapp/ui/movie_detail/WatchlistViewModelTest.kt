package com.gdomingues.androidapp.ui.movie_detail

import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import com.gdomingues.androidapp.domain.ToggleWatchlistUseCase
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class WatchlistViewModelTest {

    private lateinit var viewModel: WatchlistViewModel
    private val toggleUseCase: ToggleWatchlistUseCase = mock()
    private val repository: WatchlistRepository = mock()

    private val testMovie = TrendingMovieUiModel(
        id = 101,
        title = "Inception",
        overview = "A thief enters dreams",
        backdropPath = "/inception.jpg",
        voteAverage = 8.8,
        releaseDate = null
    )

    @Before
    fun setup() {
        viewModel = WatchlistViewModel(toggleUseCase, repository)
    }

    @Test
    fun `isInWatchlist returns true if movie is in watchlist`() = runTest {
        // Given
        whenever(repository.isInWatchlist(testMovie.id)).thenReturn(true)

        // When
        val result = viewModel.isInWatchlist(testMovie.id)

        // Then
        assertTrue(result)
        verify(repository).isInWatchlist(testMovie.id)
    }

    @Test
    fun `isInWatchlist returns false if movie is not in watchlist`() = runTest {
        // Given
        whenever(repository.isInWatchlist(testMovie.id)).thenReturn(false)

        // When
        val result = viewModel.isInWatchlist(testMovie.id)

        // Then
        assertFalse(result)
        verify(repository).isInWatchlist(testMovie.id)
    }

    @Test
    fun `toggle calls use case and returns result`() = runTest {
        // Given
        whenever(toggleUseCase.invoke(testMovie)).thenReturn(true)

        // When
        val result = viewModel.toggle(testMovie)

        // Then
        assertTrue(result)
        verify(toggleUseCase).invoke(testMovie)
    }
}
