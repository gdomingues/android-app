package com.gdomingues.androidapp.ui.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gdomingues.androidapp.ui.movie_detail.MovieDetailScreen
import com.gdomingues.androidapp.ui.selected_movie.SelectedMovieViewModel
import com.gdomingues.androidapp.ui.trending_movies.TrendingMoviesScreen
import com.gdomingues.androidapp.ui.trending_movies.TrendingMoviesViewModel
import com.gdomingues.androidapp.ui.watchlist.WatchlistScreen
import com.gdomingues.androidapp.ui.watchlist.WatchlistViewModelImpl

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route ?: ""

    val bottomBarRoutes = setOf(
        BottomNavItem.Trending.route,
        BottomNavItem.Watchlist.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                AppBottomBar(currentDestination = currentRoute) { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Trending.route,
            modifier = Modifier.padding(innerPadding),
            route = "root"
        ) {
            composable(BottomNavItem.Trending.route) { navBackStackEntry ->
                val parentEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry("root")
                }

                val viewModel: TrendingMoviesViewModel = hiltViewModel()
                val sharedViewModel: SelectedMovieViewModel = hiltViewModel(parentEntry)
                val uiState by viewModel.uiState.collectAsState()

                TrendingMoviesScreen(
                    uiState = uiState,
                    onMovieClick = { movie ->
                        sharedViewModel.selectMovie(movie)
                        navController.navigate("movie_detail")
                    },
                    onRetry = viewModel::fetchMovies,
                    onObserverListScroll = viewModel::observeListScroll
                )
            }

            composable("movie_detail") { navBackStackEntry ->
                val parentEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry("root")
                }

                val sharedViewModel: SelectedMovieViewModel = hiltViewModel(parentEntry)
                val watchlistViewModel: WatchlistViewModelImpl = hiltViewModel()
                val movie by sharedViewModel.selectedMovie.collectAsState()

                MovieDetailScreen(
                    movie = movie,
                    watchlistViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(BottomNavItem.Watchlist.route) { navBackStackEntry ->
                val parentEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry("root")
                }

                val viewModel: WatchlistViewModelImpl = hiltViewModel()
                val movies by viewModel.watchlist.collectAsState()
                val sharedViewModel: SelectedMovieViewModel = hiltViewModel(parentEntry)

                WatchlistScreen(
                    movies = movies,
                    onMovieClick = { movie ->
                        sharedViewModel.selectMovie(movie)
                        navController.navigate("movie_detail")
                    }
                )
            }
        }
    }
}
