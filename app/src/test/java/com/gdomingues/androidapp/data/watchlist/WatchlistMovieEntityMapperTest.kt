package com.gdomingues.androidapp.data.watchlist

import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class WatchlistMapperTest {

    @Test
    fun `trendingMovieUiModel should map to entity and back correctly`() {
        val original = TrendingMovie(
            id = 1,
            title = "Interstellar",
            overview = "Space mission to save humanity",
            backdropPath = "/interstellar.jpg",
            voteAverage = 8.6,
            releaseDate = LocalDate.of(2014, 11, 7)
        )

        val entity = original.toEntity()
        val result = entity.toDomainModel()

        assertEquals(original, result)
    }

    @Test
    fun `null releaseDate is preserved through mapping`() {
        val original = TrendingMovie(
            id = 2,
            title = "Unknown Movie",
            overview = "Mystery",
            backdropPath = "/unknown_movie.jpg",
            voteAverage = 5.0,
            releaseDate = null
        )

        val entity = original.toEntity()
        val result = entity.toDomainModel()

        assertEquals(original, result)
    }
}
