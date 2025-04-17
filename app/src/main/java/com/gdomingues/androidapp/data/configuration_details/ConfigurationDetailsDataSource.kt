package com.gdomingues.androidapp.data.configuration_details

import com.gdomingues.androidapp.data.MoviesApi
import com.gdomingues.androidapp.data.di.IODispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConfigurationDetailsDataSource @Inject constructor(
    private val moviesApi: MoviesApi,
    @IODispatcherQualifier private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getConfigurationDetails(): ConfigurationDetails {
        return withContext(ioDispatcher) {
            val configurationDetailsResponse = moviesApi.getConfiguration()
            configurationDetailsResponse.toDomain()
        }
    }
}
