package com.gdomingues.androidapp.data.configuration_details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Optional
import javax.inject.Inject

class ConfigurationDetailsRepository @Inject constructor(
    private val configurationDetailsDataSource: ConfigurationDetailsDataSource,
    private val externalScope: CoroutineScope
) {
    private val configurationDetailsMutex = Mutex()
    private var configurationDetails: Optional<ConfigurationDetails> = Optional.empty()

    suspend fun getConfigurationDetails(): ConfigurationDetails {
        return if (configurationDetails.isEmpty) {
            externalScope.async {
                configurationDetailsDataSource.getConfigurationDetails().also { networkResult ->
                    configurationDetailsMutex.withLock {
                        configurationDetails = Optional.of(networkResult)
                    }
                }
            }.await()
        } else {
            configurationDetailsMutex.withLock { this.configurationDetails.get() }
        }
    }
}
