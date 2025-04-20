package com.gdomingues.androidapp.domain

import com.gdomingues.androidapp.data.configuration_details.ConfigurationDetailsRepository
import com.gdomingues.androidapp.data.trending_movies.TrendingMoviesRepository
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import com.gdomingues.androidapp.ui.trending_movies.TrendingMoviesUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val trendingMoviesRepository: TrendingMoviesRepository,
    private val configurationDetailsRepository: ConfigurationDetailsRepository,
    private val uriBuilder: UriBuilder,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(page: Int): TrendingMoviesUiModel {
        return withContext(defaultDispatcher) {
            val configurationDetails = configurationDetailsRepository.getConfigurationDetails()
            val trendingMovies = trendingMoviesRepository.getTrendingMovies(page)
            val backdropSizes = configurationDetails.images.backdropSizes

            val movies = trendingMovies.movies.map { trendingMovie ->
                val backdropPath = trendingMovie.backdropPath
                val baseUrl = configurationDetails.images.secureBaseUrl

                val fullBackdropPath = uriBuilder.buildUri(
                    baseUrl,
                    backdropSizes,
                    backdropPath
                ).toString()

                TrendingMovieUiModel(
                    trendingMovie.id,
                    trendingMovie.title,
                    trendingMovie.overview,
                    fullBackdropPath,
                    trendingMovie.voteAverage,
                    trendingMovie.releaseDate
                )
            }

            TrendingMoviesUiModel(trendingMovies.totalPages, movies)
        }
    }
}
