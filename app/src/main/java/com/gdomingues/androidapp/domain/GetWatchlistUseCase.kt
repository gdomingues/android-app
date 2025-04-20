package com.gdomingues.androidapp.domain

import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWatchlistUseCase @Inject constructor(
    private val repository: WatchlistRepository
) {
    operator fun invoke(): Flow<List<TrendingMovieUiModel>> {
        return repository.getWatchlist().map { list ->
            list.map { it.toUiModel() }
        }
    }

    private fun TrendingMovie.toUiModel(): TrendingMovieUiModel {
        return TrendingMovieUiModel(
            id = id,
            title = title,
            overview = overview,
            backdropPath = backdropPath,
            voteAverage = voteAverage,
            releaseDate = releaseDate
        )
    }
}
