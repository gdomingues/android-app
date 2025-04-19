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
    private val datasource = TrendingMoviesDatasource(
        moviesApi = moviesApi,
        ioDispatcher = testDispatcher
    )

    @Test
    fun `getTrendingMovies returns mapped domain model`() = runTest(testDispatcher) {
        // Given
        val page = 1
        val trendingMovieResponses = listOf(
            TrendingMovieResponse(
                adult = false,
                backdropPath = "/path.jpg",
                id = 1,
                title = "Movie One",
                originalLanguage = "en",
                originalTitle = "Movie One Original",
                overview = "Overview for Movie One",
                posterPath = "/poster.jpg",
                mediaType = "movie",
                genreIds = listOf(28, 12),
                popularity = 80.5,
                releaseDate = "2024-10-01",
                video = false,
                voteAverage = 7.9,
                voteCount = 1200
            )
        )

        val apiResponse = TrendingMoviesResponse(
            page = 1,
            results = trendingMovieResponses,
            totalPages = 5,
            totalResults = 100
        )

        val expected = TrendingMovies(
            page = 1,
            movies = listOf(
                TrendingMovie(
                    id = 1,
                    title = "Movie One",
                    originalTitle = "Movie One Original",
                    overview = "Overview for Movie One",
                    posterPath = "/poster.jpg",
                    backdropPath = "/path.jpg",
                    genreIds = listOf(28, 12),
                    popularity = 80.5,
                    releaseDate = "2024-10-01",
                    voteAverage = 7.9,
                    voteCount = 1200,
                    isAdult = false,
                    isVideo = false,
                    mediaType = "movie"
                )
            ),
            totalPages = 5,
            totalResults = 100
        )

        whenever(moviesApi.getTrendingMovies(page)).thenReturn(apiResponse)

        // When
        val result = datasource.getTrendingMovies(page)

        // Then
        assertEquals(expected, result)
        verify(moviesApi).getTrendingMovies(page)
    }
}
