package com.gdomingues.androidapp.data.trending_movies

import com.gdomingues.androidapp.data.MoviesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class TrendingMoviesDatasourceTest {

    private val moviesApi: MoviesApi = mock()
    private val testDispatcher = StandardTestDispatcher()
    private val datasource = TrendingMoviesDatasource(moviesApi, testDispatcher)

    @Test
    fun `getTrendingMovies returns mapped domain result`() = runTest(testDispatcher) {
        // Given
        val apiResponse = TrendingMoviesResponse(
            page = 1,
            results = listOf(
                TrendingMovieResponse(
                    adult = false,
                    backdropPath = "/path.jpg",
                    id = 42,
                    title = "Test Movie",
                    originalLanguage = "en",
                    originalTitle = "Test Movie Original",
                    overview = "A test overview",
                    posterPath = "/poster.jpg",
                    mediaType = "movie",
                    genreIds = listOf(28, 12),
                    popularity = 87.5,
                    releaseDate = "2025-01-01",
                    video = false,
                    voteAverage = 7.8,
                    voteCount = 999
                )
            ),
            totalPages = 5,
            totalResults = 50
        )

        whenever(moviesApi.getTrendingMovies()).thenReturn(apiResponse)

        // When
        val result = datasource.getTrendingMovies()

        // Then
        assertEquals(apiResponse.toDomain(), result)
        verify(moviesApi).getTrendingMovies()
    }
}
