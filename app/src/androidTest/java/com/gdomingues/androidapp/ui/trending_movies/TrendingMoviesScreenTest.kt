package com.gdomingues.androidapp.ui.trending_movies

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import org.junit.Rule
import org.junit.Test

class TrendingMoviesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleMovies = listOf(
        TrendingMovieUiModel(
            id = 1,
            title = "Movie A",
            overview = "Overview A",
            backdropPath = "https://image.tmdb.org/t/p/original/ce3prrjh9ZehEl5JinNqr4jIeaB.jpg",
            voteAverage = 7.5
        ),
        TrendingMovieUiModel(
            id = 2,
            title = "Movie B",
            overview = "Overview B",
            backdropPath = "https://image.tmdb.org/t/p/original/ce3prrjh9ZehEl5JinNqr4jIeaB.jpg",
            voteAverage = 8.3
        )
    )

    @Test
    fun showsLoadingIndicatorWhenInLoadingState() {
        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Loading,
                onMovieClick = {},
                onRetry = {}
            )
        }

        composeTestRule
            .onNodeWithTag("Loading")
            .assertIsDisplayed()
    }

    @Test
    fun showsMoviesWhenInSuccessState() {
        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Success(sampleMovies),
                onMovieClick = {},
                onRetry = {}
            )
        }

        sampleMovies.forEach { movie ->
            composeTestRule
                .onNodeWithText(movie.title)
                .assertIsDisplayed()
        }

        composeTestRule.onNodeWithText("★ 7.5").assertIsDisplayed()
        composeTestRule.onNodeWithText("★ 8.3").assertIsDisplayed()
    }

    @Test
    fun showsErrorAndCallsRetryWhenClicked() {
        var retried = false

        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Error("Oops! Something went wrong."),
                onMovieClick = {},
                onRetry = { retried = true }
            )
        }

        composeTestRule.onNodeWithText("Oops! Something went wrong.").assertIsDisplayed()

        composeTestRule.onNodeWithText("Retry").performClick()

        assert(retried)
    }

    @Test
    fun movieClickTriggersCallback() {
        var clickedMovieId: Int? = null

        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Success(sampleMovies),
                onMovieClick = { clickedMovieId = it },
                onRetry = {}
            )
        }

        composeTestRule
            .onNodeWithText("Movie A")
            .performClick()

        assert(clickedMovieId == 1)
    }

    @Test
    fun scrollsToLastMovie() {
        val largeMovieList = (1..20).map {
            TrendingMovieUiModel(
                id = it,
                title = "Movie $it",
                overview = "Overview $it",
                backdropPath = "https://image.tmdb.org/t/p/original/ce3prrjh9ZehEl5JinNqr4jIeaB.jpg",
                voteAverage = 6.0 + (it % 5)
            )
        }

        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Success(largeMovieList),
                onMovieClick = {},
                onRetry = {}
            )
        }

        composeTestRule
            .onNodeWithTag("TrendingMoviesList")
            .performScrollToNode(hasText("Movie 20"))

        composeTestRule.onNodeWithText("Movie 20").assertIsDisplayed()
    }
}
