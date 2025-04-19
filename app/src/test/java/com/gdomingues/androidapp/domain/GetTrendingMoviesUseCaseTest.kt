package com.gdomingues.androidapp.domain

import android.net.Uri
import com.gdomingues.androidapp.data.configuration_details.ConfigurationDetails
import com.gdomingues.androidapp.data.configuration_details.ConfigurationDetailsRepository
import com.gdomingues.androidapp.data.configuration_details.ImagesConfiguration
import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.trending_movies.TrendingMovies
import com.gdomingues.androidapp.data.trending_movies.TrendingMoviesRepository
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetTrendingMoviesUseCaseTest {

    private val dispatcher = StandardTestDispatcher()
    private val trendingMoviesRepository: TrendingMoviesRepository = mock()
    private val configurationDetailsRepository: ConfigurationDetailsRepository = mock()
    private val uriBuilder: UriBuilder = mock()

    private val useCase: GetTrendingMoviesUseCase = GetTrendingMoviesUseCase(
        trendingMoviesRepository,
        configurationDetailsRepository,
        uriBuilder,
        dispatcher
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return mapped TrendingMovieUiModel list`() = runTest {
        // Given
        val config = ConfigurationDetails(
            images = ImagesConfiguration(
                baseUrl = "http://image.tmdb.org/t/p/",
                secureBaseUrl = "https://image.tmdb.org/t/p/",
                backdropSizes = listOf("w780", "w1280"),
                logoSizes = listOf("w45"),
                posterSizes = listOf("w185"),
                profileSizes = listOf("w185"),
                stillSizes = listOf("w185")
            ),
            changeKeys = listOf()
        )

        val movie = TrendingMovie(
            id = 1,
            title = "Movie Title",
            originalTitle = "Original Title",
            overview = "Some overview",
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(1, 2),
            popularity = 10.0,
            releaseDate = "2024-01-01",
            voteAverage = 8.5,
            voteCount = 1000,
            isAdult = false,
            isVideo = false,
            mediaType = "movie"
        )

        whenever(configurationDetailsRepository.getConfigurationDetails()).thenReturn(config)
        whenever(trendingMoviesRepository.getTrendingMovies()).thenReturn(
            TrendingMovies(
                page = 1,
                results = listOf(movie),
                totalPages = 1,
                totalResults = 1
            )
        )

        // Mock the URL builder to return a fixed Uri
        val mockUri = mock<Uri>()
        whenever(uriBuilder.buildUri(any(), any(), any())).thenReturn(mockUri)

        // When
        val result = useCase.invoke()

        // Then
        val expectedUiModel = TrendingMovieUiModel(
            id = 1,
            title = "Movie Title",
            overview = "Some overview",
            backdropPath = mockUri.toString(), // Expecting the mocked Uri's string representation
            voteAverage = 8.5
        )

        assertEquals(listOf(expectedUiModel), result)
    }
}
