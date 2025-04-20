package com.gdomingues.androidapp.domain

import app.cash.turbine.test
import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import kotlin.test.assertEquals

class GetWatchlistUseCaseTest {

    private val repository: WatchlistRepository = mock()
    private val useCase = GetWatchlistUseCase(repository)

    private val domainMovie = TrendingMovie(
        id = 1,
        title = "Dune",
        overview = "A sci-fi epic",
        backdropPath = "/dune.jpg",
        releaseDate = LocalDate.of(2021, 10, 22),
        voteAverage = 8.3
    )

    private val expectedUiModel = TrendingMovieUiModel(
        id = 1,
        title = "Dune",
        overview = "A sci-fi epic",
        backdropPath = "/dune.jpg",
        releaseDate = LocalDate.of(2021, 10, 22),
        voteAverage = 8.3
    )

    @Test
    fun `returns flow of UI models from repository domain list`() = runTest {
        // Given
        whenever(repository.getWatchlist()).thenReturn(flowOf(listOf(domainMovie)))

        // When
        val resultFlow = useCase()

        // Then
        resultFlow.test {
            val result = awaitItem()
            assertEquals(listOf(expectedUiModel), result)
            awaitComplete()
        }

        verify(repository).getWatchlist()
    }
}
