package com.gdomingues.androidapp.ui.movie_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun MovieDetailScreen(movie: TrendingMovieUiModel?, onBack: () -> Unit = {}) {
    if (movie == null) {
        EmptyMovieDetailScreen()
    } else {
        val viewModel: WatchlistViewModel = hiltViewModel()
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val isInWatchlist = remember { mutableStateOf(false) }

        // Recheck watchlist state on first render
        LaunchedEffect(movie.id) {
            isInWatchlist.value = viewModel.isInWatchlist(movie.id)
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            DefaultMovieDetailScreen(
                movie = movie,
                onBack = onBack,
                isInWatchlist = isInWatchlist.value,
                onToggleWatchlist = {
                    coroutineScope.launch {
                        val newState = viewModel.toggle(movie)
                        isInWatchlist.value = newState

                        val message = if (newState) {
                            "Added to watchlist"
                        } else {
                            "Removed from watchlist"
                        }

                        snackbarHostState.showSnackbar(message)
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}


@Composable
private fun DefaultMovieDetailScreen(
    movie: TrendingMovieUiModel,
    onBack: () -> Unit,
    isInWatchlist: Boolean,
    onToggleWatchlist: () -> Unit,
    modifier: Modifier = Modifier
) {
    val voteFormatted = String.format(Locale.getDefault(), "%.1f/10", movie.voteAverage)
    val releaseYear = movie.releaseDate?.year?.toString().orEmpty()
    val scope = rememberCoroutineScope()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.backdropPath)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${movie.title} backdrop",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "★ $voteFormatted",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = releaseYear,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Button(
                    onClick = {
                        scope.launch { onToggleWatchlist() }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist",
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyMovieDetailScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No movie selected",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Gray
        )
    }
}
