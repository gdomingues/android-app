package com.gdomingues.androidapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gdomingues.androidapp.data.watchlist.WatchlistDao
import com.gdomingues.androidapp.data.watchlist.WatchlistMovieEntity

@Database(entities = [WatchlistMovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}
