package com.js.movietrends.presentation.features.main.popular

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
import com.js.movietrends.presentation.components.MovieListGrid

@Composable
fun PopularScreen(viewModel: PopularViewModel = hiltViewModel()) {
    val popularMovies = viewModel.popularMovies.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (popularMovies.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator()
            }

            is LoadState.Error -> {
                Text("Error")
            }

            else -> {
                MovieListGrid(movies = popularMovies)
            }
        }
    }

}