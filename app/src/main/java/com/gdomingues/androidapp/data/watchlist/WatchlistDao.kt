package com.gdomingues.androidapp.data.watchlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist_movies")
    fun getWatchlist(): Flow<List<WatchlistMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(movie: WatchlistMovieEntity)

    @Delete
    suspend fun removeFromWatchlist(movie: WatchlistMovieEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_movies WHERE id = :movieId)")
    suspend fun isInWatchlist(movieId: Int): Boolean
}
