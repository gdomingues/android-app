package com.gdomingues.androidapp.ui.trending_movies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun TrendingMoviesScreen(
    uiState: TrendingMoviesUiState,
    onMovieClick: (Int) -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is TrendingMoviesUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is TrendingMoviesUiState.Success -> {
            LazyColumn {
                items(uiState.data) { movie ->
                    TrendingMovieItem(movie, onMovieClick)
                }
            }
        }

        is TrendingMoviesUiState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
fun TrendingMovieItem(movie: TrendingMovieUiModel, onMovieClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onMovieClick(movie.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.backdropPath)
                    .crossfade(true)
                    .build(),
                contentDescription = "${movie.title} backdrop",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rating: ${movie.voteAverage}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
