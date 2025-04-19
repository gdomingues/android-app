package com.gdomingues.androidapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gdomingues.androidapp.ui.movie_detail.MovieDetailScreen
import com.gdomingues.androidapp.ui.trending_movies.TrendingMoviesScreen
import com.gdomingues.androidapp.ui.trending_movies.TrendingMoviesViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "trending_movies") {
        composable("trending_movies") {
            val viewModel: TrendingMoviesViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            TrendingMoviesScreen(
                uiState,
                onMovieClick = { movieId ->
                    navController.navigate("movie_detail/$movieId")
                },
                onRetry = viewModel::fetchMovies
            )
        }
        composable(
            route = "movie_detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1
            MovieDetailScreen(movieId = movieId)
        }
    }
}
