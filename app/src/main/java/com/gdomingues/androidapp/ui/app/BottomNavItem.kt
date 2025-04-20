package com.gdomingues.androidapp.ui.app

sealed class BottomNavItem(val route: String, val label: String) {
    data object Trending : BottomNavItem("trending_movies", "Trending")
    data object Watchlist : BottomNavItem("watchlist", "Watchlist")
}
