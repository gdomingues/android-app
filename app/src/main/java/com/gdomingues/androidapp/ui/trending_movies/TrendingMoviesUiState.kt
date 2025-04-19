package com.gdomingues.androidapp.ui.trending_movies

sealed class TrendingMoviesUiState {
    object Loading : TrendingMoviesUiState()
    data class Success(val data: List<TrendingMovieUiModel>) : TrendingMoviesUiState()
    data class Error(val message: String) : TrendingMoviesUiState()
}
