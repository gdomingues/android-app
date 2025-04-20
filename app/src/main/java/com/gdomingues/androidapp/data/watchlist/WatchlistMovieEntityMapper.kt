package com.gdomingues.androidapp.data.watchlist

import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import java.time.LocalDate

fun TrendingMovie.toEntity(): WatchlistMovieEntity = WatchlistMovieEntity(
    id = id,
    title = title,
    overview = overview,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate?.toString()
)

fun WatchlistMovieEntity.toDomainModel(): TrendingMovie = TrendingMovie(
    id = id,
    title = title,
    overview = overview,
    backdropPath = backdropPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate?.let { LocalDate.parse(it) }
)
