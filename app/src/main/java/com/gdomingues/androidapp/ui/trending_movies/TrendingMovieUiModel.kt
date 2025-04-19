package com.gdomingues.androidapp.ui.trending_movies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrendingMovieUiModel(
    val id: Int,
    val title: String,
    val overview: String,
    val backdropPath: String,
    val voteAverage: Double
) : Parcelable
