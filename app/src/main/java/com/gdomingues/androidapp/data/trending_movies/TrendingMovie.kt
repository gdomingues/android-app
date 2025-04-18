package com.gdomingues.androidapp.data.trending_movies

data class TrendingMovie(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val genreIds: List<Int>,
    val popularity: Double,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val isAdult: Boolean,
    val isVideo: Boolean,
    val mediaType: String
)
