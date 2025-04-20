package com.gdomingues.androidapp.ui.trending_movies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingMoviesScreen(
    uiState: TrendingMoviesUiState,
    onMovieClick: (TrendingMovieUiModel) -> Unit,
    onRetry: () -> Unit,
    onObserverListScroll: (LazyListState) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    // Notify ViewModel for pagination
    LaunchedEffect(Unit) {
        onObserverListScroll(listState)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Trending Movies",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is TrendingMoviesUiState.Loading -> LoadingView()
                is TrendingMoviesUiState.Error -> ErrorView(uiState, onRetry)
                is TrendingMoviesUiState.Success -> TrendingMoviesList(
                    uiState = uiState,
                    onMovieClick = onMovieClick,
                    listState = listState
                )
            }
        }
    }
}

@Composable
private fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("Loading"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(
    uiState: TrendingMoviesUiState.Error,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(uiState.message, color = Color.Red, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.testTag("RetryButton")
            ) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun TrendingMoviesList(
    uiState: TrendingMoviesUiState.Success,
    onMovieClick: (TrendingMovieUiModel) -> Unit,
    listState: LazyListState,
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .testTag("TrendingMoviesList"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(uiState.movies.size) { index ->
            val movie = uiState.movies[index]
            TrendingMovieCard(movie, onMovieClick)
        }

        if (uiState.isLoadingMore) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .testTag("LoadingMoreProgressIndicator")
                )
            }
        }
    }
}

@Composable
fun TrendingMovieCard(movie: TrendingMovieUiModel, onMovieClick: (TrendingMovieUiModel) -> Unit) {
    val voteFormatted = String.format(Locale.getDefault(), "%.1f/10", movie.voteAverage)

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onMovieClick(movie) },
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
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "★ $voteFormatted",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
