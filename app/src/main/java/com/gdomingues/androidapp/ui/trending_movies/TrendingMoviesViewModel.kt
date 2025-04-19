package com.gdomingues.androidapp.ui.trending_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdomingues.androidapp.domain.GetTrendingMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrendingMoviesUiState>(TrendingMoviesUiState.Loading)
    val uiState: StateFlow<TrendingMoviesUiState> = _uiState

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = TrendingMoviesUiState.Loading
            try {
                val result = getTrendingMoviesUseCase()
                _uiState.value = TrendingMoviesUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = TrendingMoviesUiState.Error("Something went wrong: ${e.message}")
            }
        }
    }
}
