package com.gdomingues.androidapp.data.trending_movies

data class TrendingMovies(
    val page: Int,
    val movies: List<TrendingMovie>,
    val totalPages: Int,
    val totalResults: Int
)
