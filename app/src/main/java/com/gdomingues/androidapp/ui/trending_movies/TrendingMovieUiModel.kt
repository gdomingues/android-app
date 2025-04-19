package com.gdomingues.androidapp.ui.trending_movies

data class TrendingMovieUiModel(
    val id: Int,
    val title: String,
    val overview: String,
    val backdropPath: String,
    val voteAverage: Double
)
