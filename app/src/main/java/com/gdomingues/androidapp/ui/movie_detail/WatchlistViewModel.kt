package com.gdomingues.androidapp.ui.movie_detail

import androidx.lifecycle.ViewModel
import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import com.gdomingues.androidapp.domain.ToggleWatchlistUseCase
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val toggleWatchlist: ToggleWatchlistUseCase,
    private val repository: WatchlistRepository
) : ViewModel() {

    suspend fun isInWatchlist(movieId: Int): Boolean {
        return repository.isInWatchlist(movieId)
    }

    suspend fun toggle(movie: TrendingMovieUiModel): Boolean {
        return toggleWatchlist(movie)
    }
}
