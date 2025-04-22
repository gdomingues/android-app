package com.gdomingues.androidapp.ui.movie_detail

import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import com.gdomingues.androidapp.ui.watchlist.WatchlistViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeWatchlistViewModel : WatchlistViewModel() {

    override val watchlist: StateFlow<List<TrendingMovieUiModel>> =
        MutableStateFlow(emptyList())

    override suspend fun isInWatchlist(movieId: Int): Boolean {
        return false
    }

    override suspend fun toggle(movie: TrendingMovieUiModel): Boolean {
        return true
    }
}
