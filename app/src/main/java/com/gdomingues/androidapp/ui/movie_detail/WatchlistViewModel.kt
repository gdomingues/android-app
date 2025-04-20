package com.gdomingues.androidapp.ui.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.watchlist.WatchlistRepository
import com.gdomingues.androidapp.domain.GetWatchlistUseCase
import com.gdomingues.androidapp.domain.ToggleWatchlistUseCase
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    getWatchlist: GetWatchlistUseCase,
    private val toggleWatchlist: ToggleWatchlistUseCase,
    private val repository: WatchlistRepository
) : ViewModel() {

    val watchlist: StateFlow<List<TrendingMovie>> = getWatchlist()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    suspend fun isInWatchlist(movieId: Int): Boolean {
        return repository.isInWatchlist(movieId)
    }

    suspend fun toggle(movie: TrendingMovieUiModel): Boolean {
        return toggleWatchlist(movie)
    }
}
