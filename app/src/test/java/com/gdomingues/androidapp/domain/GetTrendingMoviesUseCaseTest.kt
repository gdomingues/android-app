package com.gdomingues.androidapp.domain

import android.net.Uri
import com.gdomingues.androidapp.data.configuration_details.ConfigurationDetails
import com.gdomingues.androidapp.data.configuration_details.ConfigurationDetailsRepository
import com.gdomingues.androidapp.data.configuration_details.ImagesConfiguration
import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.trending_movies.TrendingMovies
import com.gdomingues.androidapp.data.trending_movies.TrendingMoviesRepository
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import com.gdomingues.androidapp.ui.trending_movies.TrendingMoviesUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetTrendingMoviesUseCaseTest {

    private val dispatcher = StandardTestDispatcher()
    private val trendingMoviesRepository: TrendingMoviesRepository = mock()
    private val configurationDetailsRepository: ConfigurationDetailsRepository = mock()
    private val uriBuilder: UriBuilder = mock()

    private val useCase = GetTrendingMoviesUseCase(
        trendingMoviesRepository = trendingMoviesRepository,
        configurationDetailsRepository = configurationDetailsRepository,
        uriBuilder = uriBuilder,
        defaultDispatcher = dispatcher
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
    fun `invoke returns mapped TrendingMoviesUiModel`() = runTest {
        // Given
        val page = 1

        val movie = TrendingMovie(
            id = 1,
            title = "Movie Title",
            overview = "Overview",
            backdropPath = "/backdrop.jpg",
            releaseDate = LocalDate.of(2024, 1, 1),
            voteAverage = 8.7
        )

        val trendingMovies = TrendingMovies(
            page = 1,
            movies = listOf(movie),
            totalPages = 5,
            totalResults = 100
        )

        val config = ConfigurationDetails(
            images = ImagesConfiguration(
                baseUrl = "http://image.tmdb.org/t/p/",
                secureBaseUrl = "https://image.tmdb.org/t/p/",
                backdropSizes = listOf("w780"),
                logoSizes = emptyList(),
                posterSizes = emptyList(),
                profileSizes = emptyList(),
                stillSizes = emptyList()
            ),
            changeKeys = emptyList()
        )


        whenever(configurationDetailsRepository.getConfigurationDetails()).thenReturn(config)
        whenever(trendingMoviesRepository.getTrendingMovies(page)).thenReturn(trendingMovies)

        val mockUri = mock<Uri>()
        whenever(uriBuilder.buildUri(any(), any(), any())).thenReturn(mockUri)

        // When
        val result = useCase.invoke(page)

        // Then
        val expectedUiModel = TrendingMoviesUiModel(
            totalPages = 5,
            movies = listOf(
                TrendingMovieUiModel(
                    id = 1,
                    title = "Movie Title",
                    overview = "Overview",
                    backdropPath = mockUri.toString(),
                    voteAverage = 8.7,
                    releaseDate = LocalDate.of(2024, 1, 1)
                )
            )
        )

        assertEquals(expectedUiModel, result)
    }
}
