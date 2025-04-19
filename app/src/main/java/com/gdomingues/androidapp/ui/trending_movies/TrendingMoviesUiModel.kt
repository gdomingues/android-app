package com.gdomingues.androidapp.ui.trending_movies

data class TrendingMoviesUiModel(
    val totalPages: Int,
    val movies: List<TrendingMovieUiModel>,
)
