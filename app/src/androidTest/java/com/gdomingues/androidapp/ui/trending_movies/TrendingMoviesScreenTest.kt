package com.gdomingues.androidapp.ui.trending_movies

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class TrendingMoviesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleMovies = (1..20).map {
        TrendingMovieUiModel(
            id = it,
            title = "Movie $it",
            overview = "Overview $it",
            backdropPath = "https://image.tmdb.org/t/p/original/ce3prrjh9ZehEl5JinNqr4jIeaB.jpg",
            voteAverage = 7.0 + (it % 5)
        )
    }

    @Test
    fun showsLoadingIndicatorWhenStateIsLoading() {
        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Loading,
                onMovieClick = {},
                onRetry = {},
                onObserverListScroll = {}
            )
        }

        composeTestRule.onNodeWithTag("Loading").assertIsDisplayed()
    }

    @Test
    fun showsErrorAndRetryButtonWhenStateIsError() {
        var retried = false

        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Error("Something went wrong"),
                onMovieClick = {},
                onRetry = { retried = true },
                onObserverListScroll = {}
            )
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
        composeTestRule.onNodeWithTag("RetryButton").assertIsDisplayed().performClick()

        assertTrue(retried)
    }

    @Test
    fun showsMovieListWhenStateIsSuccess() {
        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Success(
                    sampleMovies.take(5),
                    isLoadingMore = false
                ),
                onMovieClick = {},
                onRetry = {},
                onObserverListScroll = {}
            )
        }

        composeTestRule.onNodeWithTag("TrendingMoviesList").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movie 1").assertIsDisplayed()
    }

    @Test
    fun triggersMovieClickWhenCardIsClicked() {
        var clickedId: Int? = null

        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Success(
                    sampleMovies.take(1),
                    isLoadingMore = false
                ),
                onMovieClick = { clickedId = it },
                onRetry = {},
                onObserverListScroll = {}
            )
        }

        composeTestRule.onNodeWithText("Movie 1").performClick()
        assertTrue(clickedId == 1)
    }

    @Test
    fun scrollsToLastMovieInLargeList() {
        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Success(sampleMovies, isLoadingMore = false),
                onMovieClick = {},
                onRetry = {},
                onObserverListScroll = {}
            )
        }

        composeTestRule.onNodeWithTag("TrendingMoviesList")
            .performScrollToNode(hasText("Movie 20"))

        composeTestRule.onNodeWithText("Movie 20").assertIsDisplayed()
    }

    @Test
    fun showsLoadingMoreIndicatorWhenIsLoadingMoreIsTrue() {
        composeTestRule.setContent {
            TrendingMoviesScreen(
                uiState = TrendingMoviesUiState.Success(sampleMovies.take(5), isLoadingMore = true),
                onMovieClick = {},
                onRetry = {},
                onObserverListScroll = {}
            )
        }

        composeTestRule.onNodeWithTag("TrendingMoviesList")
            .performScrollToNode(hasTestTag("LoadingMoreProgressIndicator"))

        composeTestRule.onNodeWithTag("LoadingMoreProgressIndicator").assertIsDisplayed()
    }
}
