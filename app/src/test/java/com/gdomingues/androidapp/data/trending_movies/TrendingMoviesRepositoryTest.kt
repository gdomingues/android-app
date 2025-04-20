package com.gdomingues.androidapp.data.trending_movies

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
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
                    overview = "Test overview",
                    backdropPath = "/backdrop.jpg",
                    releaseDate = LocalDate.of(2024, 1, 1),
                    voteAverage = 8.5
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
