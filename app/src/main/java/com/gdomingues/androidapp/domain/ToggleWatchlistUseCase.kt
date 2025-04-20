package com.gdomingues.androidapp.domain

import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(movie: TrendingMovieUiModel): Boolean {
        val domainMovie = movie.toDomain()
        return withContext(defaultDispatcher) {
            repository.toggleWatchlist(domainMovie)
        }
    }

    private fun TrendingMovieUiModel.toDomain(): TrendingMovie {
        return TrendingMovie(
            id = id,
            title = title,
            overview = overview,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage
        )
    }
}
