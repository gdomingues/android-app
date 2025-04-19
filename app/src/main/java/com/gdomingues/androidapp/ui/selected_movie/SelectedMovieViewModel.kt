package com.gdomingues.androidapp.ui.selected_movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SelectedMovieViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val selectedMovie: StateFlow<TrendingMovieUiModel?> =
        savedStateHandle.getStateFlow("selected_movie", null)

    fun selectMovie(movie: TrendingMovieUiModel) {
        savedStateHandle["selected_movie"] = movie
    }
}
