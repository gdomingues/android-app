package com.gdomingues.androidapp.data.trending_movies

class TrendingMoviesRepository(
    private val trendingMoviesDatasource: TrendingMoviesDatasource
) {
    suspend fun getTrendingMovies(): TrendingMovies {
        return trendingMoviesDatasource.getTrendingMovies()
    }
}
