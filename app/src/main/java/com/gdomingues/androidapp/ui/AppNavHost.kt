package com.gdomingues.androidapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gdomingues.androidapp.ui.movie_detail.MovieDetailScreen
import com.gdomingues.androidapp.ui.selected_movie.SelectedMovieViewModel
import com.gdomingues.androidapp.ui.trending_movies.TrendingMoviesScreen
import com.gdomingues.androidapp.ui.trending_movies.TrendingMoviesViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "trending_movies",
        route = "root"
    ) {
        composable("trending_movies") { navBackStackEntry ->
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
            val movie by sharedViewModel.selectedMovie.collectAsState()

            MovieDetailScreen(
                movie = movie,
                onBack = { navController.popBackStack() }
            )
        }
    }

}
