package com.gdomingues.androidapp.data.trending_movies

import com.squareup.moshi.Json

data class TrendingMovieResponse(
    @Json(name = "adult") val adult: Boolean,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String?,
    @Json(name = "original_language") val originalLanguage: String,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "media_type") val mediaType: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>,
    @Json(name = "popularity") val popularity: Double,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "video") val video: Boolean,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int
)
