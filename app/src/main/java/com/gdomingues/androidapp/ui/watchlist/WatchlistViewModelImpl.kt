package com.gdomingues.androidapp.ui.watchlist

import androidx.lifecycle.viewModelScope
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
class WatchlistViewModelImpl @Inject constructor(
    getWatchlist: GetWatchlistUseCase,
    private val toggleWatchlist: ToggleWatchlistUseCase,
    private val repository: WatchlistRepository
) : WatchlistViewModel() {

    override val watchlist: StateFlow<List<TrendingMovieUiModel>> = getWatchlist()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    override suspend fun isInWatchlist(movieId: Int): Boolean {
        return repository.isInWatchlist(movieId)
    }

    override suspend fun toggle(movie: TrendingMovieUiModel): Boolean {
        return toggleWatchlist(movie)
    }
}
