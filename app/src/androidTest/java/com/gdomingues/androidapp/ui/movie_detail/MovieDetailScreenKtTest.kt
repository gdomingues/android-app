package com.gdomingues.androidapp.ui.movie_detail

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import com.gdomingues.androidapp.ui.watchlist.WatchlistModule
import com.gdomingues.androidapp.ui.watchlist.WatchlistViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@UninstallModules(WatchlistModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MovieDetailScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @BindValue
    @JvmField
    val fakeViewModel: WatchlistViewModel = FakeWatchlistViewModel()

    private val sampleMovie = TrendingMovieUiModel(
        id = 1,
        title = "Test Movie",
        overview = "This is a test movie.",
        backdropPath = "https://example.com/backdrop.jpg",
        voteAverage = 8.3,
        releaseDate = LocalDate.of(2023, 5, 1)
    )

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun showsEmptyMovieScreenWhenMovieIsNull() {
        composeTestRule.setContent {
            MovieDetailScreen(
                movie = null,
                viewModel = fakeViewModel
            )
        }

        composeTestRule.onNodeWithText("No movie selected")
            .assertIsDisplayed()
    }

    @Test
    fun showsMovieDetailsWhenMovieIsNotNull() {
        composeTestRule.setContent {
            MovieDetailScreen(
                movie = sampleMovie,
                viewModel = fakeViewModel
            )
        }

        composeTestRule.onNodeWithText("Test Movie").assertIsDisplayed()
        composeTestRule.onNodeWithText("★ 8.3/10").assertIsDisplayed()
        composeTestRule.onNodeWithText("2023").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a test movie.").assertIsDisplayed()
    }

    @Test
    fun togglesWatchlistButtonText() {
        composeTestRule.setContent {
            MovieDetailContent(
                movie = sampleMovie,
                voteFormatted = "8.3/10",
                releaseYear = "2023",
                scope = rememberCoroutineScope(),
                isInWatchlist = false,
                onToggleWatchlist = { }
            )
        }

        composeTestRule.onNodeWithText("Add to Watchlist").assertIsDisplayed()
    }

    @Test
    fun showsBackButtonAndCallsCallback() {
        var backCalled = false

        composeTestRule.setContent {
            MovieDetailHeader(movie = sampleMovie, onBack = { backCalled = true })
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assert(backCalled)
    }
}
