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
    private val repository = TrendingMoviesRepository(datasource)
    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `getTrendingMovies returns data from datasource`() = runTest(testDispatcher) {
        // Given
        val page = 1
        val expected = TrendingMovies(
            page = page,
            movies = listOf(
                TrendingMovie(
                    id = 1,
                    title = "Test Movie",
                    originalTitle = "Original Title",
                    overview = "Test overview",
                    posterPath = "/poster.jpg",
                    backdropPath = "/backdrop.jpg",
                    genreIds = listOf(28, 12),
                    popularity = 99.9,
                    releaseDate = "2024-01-01",
                    voteAverage = 8.5,
                    voteCount = 1000,
                    isAdult = false,
                    isVideo = false,
                    mediaType = "movie"
                )
            ),
            totalPages = 10,
            totalResults = 100
        )

        whenever(datasource.getTrendingMovies(page)).thenReturn(expected)

        // When
        val result = repository.getTrendingMovies(page)

        // Then
        assertEquals(expected, result)
        verify(datasource).getTrendingMovies(page)
    }
}
