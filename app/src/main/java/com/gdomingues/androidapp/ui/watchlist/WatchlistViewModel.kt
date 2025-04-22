package com.gdomingues.androidapp.ui.watchlist

import androidx.lifecycle.ViewModel
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.flow.StateFlow

abstract class WatchlistViewModel : ViewModel() {
    abstract val watchlist: StateFlow<List<TrendingMovieUiModel>>

    abstract suspend fun isInWatchlist(movieId: Int): Boolean

    abstract suspend fun toggle(movie: TrendingMovieUiModel): Boolean
}
