package com.gdomingues.androidapp.ui.watchlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class WatchlistScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val movie1 = TrendingMovieUiModel(
        id = 1,
        title = "Movie 1",
        overview = "Overview 1",
        backdropPath = "https://image.tmdb.org/t/p/original/1.jpg",
        voteAverage = 7.5,
        releaseDate = LocalDate.of(2024, 1, 1)
    )

    private val movie2 = TrendingMovieUiModel(
        id = 2,
        title = "Movie 2",
        overview = "Overview 2",
        backdropPath = "https://image.tmdb.org/t/p/original/2.jpg",
        voteAverage = 8.0,
        releaseDate = LocalDate.of(2024, 2, 1)
    )

    private val sampleMovies = listOf(movie1, movie2)

    @Test
    fun showsEmptyWatchlistMessageWhenListIsEmpty() {
        composeTestRule.setContent {
            WatchlistScreen(movies = emptyList(), onMovieClick = {})
        }

        composeTestRule.onNodeWithText("Your watchlist is empty").assertIsDisplayed()
    }

    @Test
    fun showsAllMoviesInWatchlist() {
        composeTestRule.setContent {
            WatchlistScreen(movies = sampleMovies, onMovieClick = {})
        }

        sampleMovies.forEach { movie ->
            composeTestRule.onNodeWithText(movie.title).assertIsDisplayed()
        }
    }

    @Test
    fun triggersOnMovieClickWhenCardIsClicked() {
        var clickedMovie: TrendingMovieUiModel? = null

        composeTestRule.setContent {
            WatchlistScreen(
                movies = sampleMovies,
                onMovieClick = { clickedMovie = it }
            )
        }

        composeTestRule.onNodeWithText("Movie 1").performClick()
        assertEquals(movie1, clickedMovie)
    }

    @Test
    fun updatesFromEmptyToNonEmptyWatchlist() {
        lateinit var watchlistState: MutableState<List<TrendingMovieUiModel>>

        composeTestRule.setContent {
            watchlistState = remember { mutableStateOf(emptyList()) }

            WatchlistScreen(
                movies = watchlistState.value,
                onMovieClick = {}
            )
        }

        // Initially shows empty message
        composeTestRule.onNodeWithText("Your watchlist is empty").assertIsDisplayed()

        // Simulate watchlist update
        composeTestRule.runOnUiThread {
            watchlistState.value = sampleMovies
        }

        // Now movies should be displayed
        composeTestRule.onNodeWithText("Movie 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movie 2").assertIsDisplayed()
    }

    @Test
    fun updatesFromNonEmptyToEmptyWatchlist() {
        lateinit var watchlistState: MutableState<List<TrendingMovieUiModel>>

        composeTestRule.setContent {
            watchlistState = remember { mutableStateOf(sampleMovies) }

            WatchlistScreen(
                movies = watchlistState.value,
                onMovieClick = {}
            )
        }

        // Initially shows movies
        composeTestRule.onNodeWithText("Movie 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Movie 2").assertIsDisplayed()

        // Simulate removal of all movies
        composeTestRule.runOnUiThread {
            watchlistState.value = emptyList()
        }

        // Now it should show empty message
        composeTestRule.onNodeWithText("Your watchlist is empty").assertIsDisplayed()
    }
}
