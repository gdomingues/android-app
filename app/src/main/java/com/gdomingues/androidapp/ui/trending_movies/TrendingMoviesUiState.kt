package com.gdomingues.androidapp.ui.trending_movies

sealed class TrendingMoviesUiState {
    data object Loading : TrendingMoviesUiState()
    data class Error(val message: String) : TrendingMoviesUiState()
    data class Success(
        val movies: List<TrendingMovieUiModel>,
        val isLoadingMore: Boolean = false,
    ) : TrendingMoviesUiState()
}
