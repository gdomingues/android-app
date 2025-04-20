package com.gdomingues.androidapp.ui.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppBottomBar(
    currentDestination: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        listOf(BottomNavItem.Trending, BottomNavItem.Watchlist).forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = when (item) {
                            is BottomNavItem.Trending -> Icons.AutoMirrored.Filled.TrendingUp
                            is BottomNavItem.Watchlist -> Icons.Default.Favorite
                        },
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}
