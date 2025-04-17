package com.gdomingues.androidapp.data

import com.gdomingues.androidapp.data.configuration_details.ConfigurationDetailsResponse
import retrofit2.http.GET

interface MoviesApi {

    /**
     * Query the API configuration details
     *
     * https://developer.themoviedb.org/reference/configuration-details
     */
    @GET("configuration")
    suspend fun getConfiguration(): ConfigurationDetailsResponse
}
