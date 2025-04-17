package com.gdomingues.androidapp.data.configuration_details

import com.gdomingues.androidapp.data.MoviesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ConfigurationDetailsDataSourceTest {

    private val moviesApi: MoviesApi = mock(MoviesApi::class.java)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var dataSource: ConfigurationDetailsDataSource

    @Before
    fun setUp() {
        dataSource = ConfigurationDetailsDataSource(moviesApi, testDispatcher)
    }

    @Test
    fun `getConfigurationDetails returns mapped domain model`() = runTest(testDispatcher) {
        // Given
        val apiResponse = ConfigurationDetailsResponse(
            images = ImagesConfigurationResponse(
                baseUrl = "http://base.url/",
                secureBaseUrl = "https://secure.url/",
                backdropSizes = listOf("w300", "w780"),
                logoSizes = listOf("w45", "w92"),
                posterSizes = listOf("w92", "w154"),
                profileSizes = listOf("w45", "w185"),
                stillSizes = listOf("w92", "w185")
            ),
            changeKeys = listOf("key1", "key2")
        )

        val expectedDomain = apiResponse.toDomain()

        // When
        whenever(moviesApi.getConfiguration()).thenReturn(apiResponse)

        // Then
        val result = dataSource.getConfigurationDetails()
        assertEquals(expectedDomain, result)
    }
}
