package com.gdomingues.androidapp.data.watchlist

import com.gdomingues.androidapp.data.trending_movies.TrendingMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WatchlistRepository @Inject constructor(private val dao: WatchlistDao) {

    fun getWatchlist(): Flow<List<TrendingMovie>> =
        dao.getWatchlist().map { list -> list.map { it.toDomainModel() } }

    suspend fun toggleWatchlist(movie: TrendingMovie): Boolean {
        if (dao.isInWatchlist(movie.id)) {
            dao.removeFromWatchlist(movie.toEntity())
            return false
        } else {
            dao.addToWatchlist(movie.toEntity())
            return true
        }
    }

    suspend fun isInWatchlist(movieId: Int): Boolean = dao.isInWatchlist(movieId)
}
