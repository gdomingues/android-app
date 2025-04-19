package com.gdomingues.androidapp.data.trending_movies

import com.gdomingues.androidapp.data.MoviesApi
import com.gdomingues.androidapp.data.di.IODispatcherQualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrendingMoviesDatasource @Inject constructor(
    private val moviesApi: MoviesApi,
    @IODispatcherQualifier private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getTrendingMovies(page: Int): TrendingMovies {
        return withContext(ioDispatcher) {
            val trendingMoviesResponse = moviesApi.getTrendingMovies(page)
            trendingMoviesResponse.toDomain()
        }
    }
}
