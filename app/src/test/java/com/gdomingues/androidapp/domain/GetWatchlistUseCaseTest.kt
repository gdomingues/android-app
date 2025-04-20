package com.gdomingues.androidapp.domain

import app.cash.turbine.test
import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import kotlin.test.assertEquals

class GetWatchlistUseCaseTest {

    private val repository: WatchlistRepository = mock()
    private lateinit var useCase: GetWatchlistUseCase

    private val sampleMovies = listOf(
        TrendingMovie(
            id = 1,
            title = "Dune",
            overview = "A sci-fi epic",
            backdropPath = "/dune.jpg",
            releaseDate = LocalDate.of(2021, 10, 22),
            voteAverage = 8.3
        ),
        TrendingMovie(
            id = 2,
            title = "Arrival",
            overview = "Aliens try to communicate",
            backdropPath = "/arrival.jpg",
            releaseDate = LocalDate.of(2016, 11, 11),
            voteAverage = 7.9
        )
    )

    @Before
    fun setup() {
        useCase = GetWatchlistUseCase(repository)
    }

    @Test
    fun `returns flow of watchlisted movies from repository`() = runTest {
        // Given
        whenever(repository.getWatchlist()).thenReturn(flowOf(sampleMovies))

        // When
        val resultFlow = useCase()

        // Then
        resultFlow.test {
            val result = awaitItem()
            assertEquals(sampleMovies, result)
            awaitComplete()
        }

        verify(repository).getWatchlist()
    }
}
