package com.gdomingues.androidapp.data.trending_movies

fun TrendingMoviesResponse.toDomain(): TrendingMovies {
    return TrendingMovies(
        page = page,
        totalPages = totalPages,
        totalResults = totalResults,
        results = results.map { it.toDomain() }
    )
}

fun TrendingMovieResponse.toDomain(): TrendingMovie {
    return TrendingMovie(
        id = id,
        title = title.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview,
        posterPath = posterPath.orEmpty(),
        backdropPath = backdropPath.orEmpty(),
        genreIds = genreIds,
        popularity = popularity,
        releaseDate = releaseDate.orEmpty(),
        voteAverage = voteAverage,
        voteCount = voteCount,
        isAdult = adult,
        isVideo = video,
        mediaType = mediaType.orEmpty()
    )
}
