package com.gdomingues.androidapp.ui.movie_detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieDetailScreen(movieId: Int) {
    Text(
        text = "Movie Detail for ID: $movieId",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(16.dp)
    )
}
