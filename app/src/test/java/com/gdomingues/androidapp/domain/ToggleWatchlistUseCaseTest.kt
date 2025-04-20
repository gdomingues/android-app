package com.gdomingues.androidapp.domain

import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ToggleWatchlistUseCaseTest {

    private val repository: WatchlistRepository = mock()
    private val useCase = ToggleWatchlistUseCase(repository, Dispatchers.Unconfined)

    private val testMovieUi = TrendingMovieUiModel(
        id = 42,
        title = "Blade Runner 2049",
        overview = "A young blade runner discovers a secret.",
        backdropPath = "/blade2049.jpg",
        voteAverage = 8.0,
        releaseDate = LocalDate.of(2017, 10, 6)
    )

    private val expectedDomainMovie = TrendingMovie(
        id = 42,
        title = "Blade Runner 2049",
        overview = "A young blade runner discovers a secret.",
        backdropPath = "/blade2049.jpg",
        voteAverage = 8.0,
        releaseDate = LocalDate.of(2017, 10, 6)
    )

    @Test
    fun `returns true when movie is added to watchlist`() = runTest {
        // Given
        whenever(repository.toggleWatchlist(expectedDomainMovie)).thenReturn(true)

        // When
        val result = useCase(testMovieUi)

        // Then
        assertTrue(result)
        verify(repository).toggleWatchlist(expectedDomainMovie)
    }

    @Test
    fun `returns false when movie is removed from watchlist`() = runTest {
        // Given
        whenever(repository.toggleWatchlist(expectedDomainMovie)).thenReturn(false)

        // When
        val result = useCase(testMovieUi)

        // Then
        assertFalse(result)
        verify(repository).toggleWatchlist(expectedDomainMovie)
    }
}
