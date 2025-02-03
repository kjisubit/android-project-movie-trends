package com.js.movietrends.presentation.features.main.nowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.js.movietrends.presentation.components.MovieListStaggeredGrid

@Composable
fun NowPlayingScreen(viewModel: NowPlayingViewModel = hiltViewModel()) {
    val nowPlayingMovies = viewModel.nowPlayingMovies.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (nowPlayingMovies.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator()
            }

            is LoadState.Error -> {
                MovieListStaggeredGrid(movies = nowPlayingMovies)
                Text(
                    text = "Cached",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .background(Color.Black)
                        .border(3.dp, Color.White)
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                )
            }

            else -> {
                MovieListStaggeredGrid(movies = nowPlayingMovies)
            }
        }
    }


}