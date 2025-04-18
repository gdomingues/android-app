package com.gdomingues.androidapp.data.trending_movies

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class TrendingMoviesRepositoryTest {

    private val datasource: TrendingMoviesDatasource = mock()
    private val testDispatcher = StandardTestDispatcher()
    private val repository = TrendingMoviesRepository(trendingMoviesDatasource = datasource)

    @Test
    fun `getTrendingMovies returns movies from datasource`() = runTest(testDispatcher) {
        // Given
        val expected = TrendingMovies(
            page = 1,
            results = listOf(
                TrendingMovie(
                    id = 1,
                    title = "Test Movie",
                    originalTitle = "Test Original Title",
                    overview = "This is a test movie",
                    posterPath = "/poster.jpg",
                    backdropPath = "/backdrop.jpg",
                    genreIds = listOf(28, 12),
                    popularity = 99.9,
                    releaseDate = "2025-01-01",
                    voteAverage = 8.0,
                    voteCount = 1000,
                    isAdult = false,
                    isVideo = false,
                    mediaType = "movie"
                )
            ),
            totalPages = 10,
            totalResults = 100
        )

        whenever(datasource.getTrendingMovies()).thenReturn(expected)

        // When
        val result = repository.getTrendingMovies()

        // Then
        assertEquals(expected, result)
        verify(datasource).getTrendingMovies()
    }
}
