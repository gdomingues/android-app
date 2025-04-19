package com.gdomingues.androidapp.ui.trending_movies

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdomingues.androidapp.domain.GetTrendingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrendingMoviesUiState>(TrendingMoviesUiState.Loading)
    val uiState: StateFlow<TrendingMoviesUiState> = _uiState.asStateFlow()

    private var currentPage = 1
    private var endReached = false
    private var isLoadingInitial = true

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            try {
                val trendingMovies = getTrendingMoviesUseCase(currentPage)
                _uiState.value = TrendingMoviesUiState.Success(movies = trendingMovies.movies)
                currentPage++
                isLoadingInitial = false
                if (currentPage >= trendingMovies.totalPages) {
                    endReached = true
                }
            } catch (e: Exception) {
                _uiState.value = TrendingMoviesUiState.Error("Something went wrong")
            }
        }
    }

    @OptIn(FlowPreview::class)
    fun observeListScroll(lazyListState: LazyListState) {
        viewModelScope.launch {
            snapshotFlow { lazyListState.layoutInfo }
                .map { layoutInfo ->
                    (layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: -1) to layoutInfo.totalItemsCount
                }
                .distinctUntilChanged()
                .debounce(300L)
                .collect { (lastVisible, total) ->
                    if (shouldFetchMore(lastVisible, total)) {
                        fetchMoreTrendingMovies()
                    }
                }
        }
    }

    private fun shouldFetchMore(lastVisibleItem: Int, totalItems: Int): Boolean {
        val state = _uiState.value as? TrendingMoviesUiState.Success
        return !endReached &&
                state != null &&
                !state.isLoadingMore &&
                lastVisibleItem >= totalItems - 3
    }

    fun fetchMoreTrendingMovies() {
        val currentSuccess = _uiState.value as? TrendingMoviesUiState.Success ?: return
        _uiState.value = currentSuccess.copy(isLoadingMore = true)

        viewModelScope.launch {
            try {
                val trendingMovies = getTrendingMoviesUseCase(currentPage)
                currentPage++
                if (currentPage >= trendingMovies.totalPages) {
                    endReached = true
                }

                _uiState.value = TrendingMoviesUiState.Success(
                    movies = currentSuccess.movies + trendingMovies.movies,
                    isLoadingMore = false
                )
            } catch (e: Exception) {
                _uiState.value = currentSuccess.copy(isLoadingMore = false)
            }
        }
    }
}
