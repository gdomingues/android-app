package com.gdomingues.androidapp.data.trending_movies

import javax.inject.Inject

class TrendingMoviesRepository @Inject constructor(
    private val trendingMoviesDatasource: TrendingMoviesDatasource
) {
    suspend fun getTrendingMovies(): TrendingMovies {
        return trendingMoviesDatasource.getTrendingMovies()
    }
}
