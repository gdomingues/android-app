package com.gdomingues.androidapp.ui.movie_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gdomingues.androidapp.ui.trending_movies.TrendingMovieUiModel

@Composable
fun MovieDetailScreen(movie: TrendingMovieUiModel?) {
    if (movie == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No movie selected",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(movie.title, style = MaterialTheme.typography.headlineMedium)
            Text(movie.overview, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
