package com.gdomingues.androidapp.data.watchlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist_movies")
data class WatchlistMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val backdropPath: String,
    val voteAverage: Double,
    val releaseDate: String? // Store as ISO string (LocalDate.toString())
)
