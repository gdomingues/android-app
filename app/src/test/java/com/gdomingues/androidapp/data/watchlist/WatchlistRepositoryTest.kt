package com.gdomingues.androidapp.data.watchlist

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.gdomingues.androidapp.data.AppDatabase
import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import com.gdomingues.androidapp.data.watchlist.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class WatchlistRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: WatchlistDao
    private lateinit var repository: WatchlistRepository

    private val testMovie = TrendingMovie(
        id = 101,
        title = "The Matrix",
        overview = "Simulation theory in action",
        backdropPath = "/matrix.jpg",
        voteAverage = 8.7,
        releaseDate = LocalDate.of(1999, 3, 31)
    )

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.watchlistDao()
        repository = WatchlistRepository(dao)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun `movie is added to and removed from watchlist`() = runBlocking {
        // Add
        assertTrue(repository.toggleWatchlist(testMovie))
        assertTrue(repository.isInWatchlist(testMovie.id))

        // Remove
        assertFalse(repository.toggleWatchlist(testMovie))
        assertFalse(repository.isInWatchlist(testMovie.id))
    }

    @Test
    fun `getWatchlist returns correct movie`() = runBlocking {
        repository.toggleWatchlist(testMovie) // adds movie

        val watchlist = repository.getWatchlist().first()
        assertEquals(1, watchlist.size)
        assertEquals(testMovie, watchlist[0])
    }
}
