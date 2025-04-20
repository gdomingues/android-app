package com.gdomingues.androidapp.data.trending_movies

import android.util.Log
import java.time.LocalDate

fun TrendingMoviesResponse.toDomain(): TrendingMovies {
    return TrendingMovies(
        page = page,
        totalPages = totalPages,
        totalResults = totalResults,
        movies = results.map { it.toDomain() }
    )
}

fun TrendingMovieResponse.toDomain(): TrendingMovie {
    return TrendingMovie(
        id = id,
        title = title.orEmpty(),
        overview = overview,
        backdropPath = backdropPath.orEmpty(),
        releaseDate = releaseDate.toLocalDateOrNull(),
        voteAverage = voteAverage
    )
}

fun String?.toLocalDateOrNull(): LocalDate? {
    return try {
        this?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) }
    } catch (e: Exception) {
        Log.e("Data parsing", e.message.orEmpty())
        null
    }
}
