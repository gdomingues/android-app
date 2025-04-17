package com.gdomingues.androidapp.data.configuration_details

import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ConfigurationDetailsRepositoryTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)

    private val mockDataSource: ConfigurationDetailsDataSource = mock()

    private lateinit var repository: ConfigurationDetailsRepository

    private val fakeDomainModel = ConfigurationDetails(
        images = ImagesConfiguration(
            baseUrl = "http://base/",
            secureBaseUrl = "https://secure/",
            backdropSizes = listOf("w300"),
            logoSizes = listOf("w92"),
            posterSizes = listOf("w185"),
            profileSizes = listOf("w45"),
            stillSizes = listOf("w90")
        ),
        changeKeys = listOf("change1", "change2")
    )

    @Before
    fun setup() {
        repository = ConfigurationDetailsRepository(mockDataSource, testScope)
    }

    @Test
    fun `getConfigurationDetails fetches from data source when cache is empty`() =
        testScope.runTest {
            // Given
            whenever(mockDataSource.getConfigurationDetails()).thenReturn(fakeDomainModel)

            // When
            val result = repository.getConfigurationDetails()

            // Then
            assertEquals(fakeDomainModel, result)
            verify(mockDataSource, times(1)).getConfigurationDetails()
        }

    @Test
    fun `getConfigurationDetails returns cached result on second call`() = testScope.runTest {
        // Given
        whenever(mockDataSource.getConfigurationDetails()).thenReturn(fakeDomainModel)

        // When
        val first = repository.getConfigurationDetails()
        val second = repository.getConfigurationDetails()

        // Then
        assertEquals(fakeDomainModel, first)
        assertEquals(fakeDomainModel, second)
        verify(mockDataSource, times(1)).getConfigurationDetails() // Only called once
    }
}
