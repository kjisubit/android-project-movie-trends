package com.js.movietrends.presentation.features.main.upcoming

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.js.movietrends.presentation.components.MovieListColumn

@Composable
fun UpcomingScreen(viewModel: UpcomingViewModel = hiltViewModel()) {
    val upcomingMovies = viewModel.upcomingMovies.collectAsLazyPagingItems()

    when (upcomingMovies.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoadState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error")
            }
        }

        else -> {
            MovieListColumn(movies = upcomingMovies)
        }
    }
}