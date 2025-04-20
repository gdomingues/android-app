package com.gdomingues.androidapp.data.trending_movies

import java.time.LocalDate

data class TrendingMovie(
    val id: Int,
    val title: String,
    val overview: String,
    val backdropPath: String,
    val releaseDate: LocalDate?,
    val voteAverage: Double
)
