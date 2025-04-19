package com.gdomingues.androidapp.ui.selected_movie

import androidx.lifecycle.SavedStateHandle
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SelectedMovieViewModelTest {

    private val savedStateHandle = SavedStateHandle()
    private val viewModel = SelectedMovieViewModel(savedStateHandle)

    @Test
    fun `selectedMovie is initially null`() = runTest {
        // When
        val movie = viewModel.selectedMovie.first()

        // Then
        assertNull(movie)
    }

    @Test
    fun `selectMovie updates selectedMovie`() = runTest {
        // Given
        val expected = TrendingMovieUiModel(
            id = 123,
            title = "Test Movie",
            overview = "This is a test movie.",
            backdropPath = "/test.jpg",
            voteAverage = 8.5
        )

        // When
        viewModel.selectMovie(expected)

        // Then
        val movie = viewModel.selectedMovie.first()
        assertEquals(expected, movie)
    }
}
