package com.gdomingues.androidapp.data

import com.gdomingues.androidapp.data.configuration_details.ConfigurationDetailsResponse
import com.gdomingues.androidapp.data.trending_movies.TrendingMoviesResponse
import retrofit2.http.GET

interface MoviesApi {

    /**
     * Query the API configuration details
     *
     * https://developer.themoviedb.org/reference/configuration-details
     */
    @GET("configuration")
    suspend fun getConfigurationDetails(): ConfigurationDetailsResponse

    /**
     * Get the trending movies on The Movie Database
     *
     * https://developer.themoviedb.org/reference/trending-movies
     */
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(): TrendingMoviesResponse
}
