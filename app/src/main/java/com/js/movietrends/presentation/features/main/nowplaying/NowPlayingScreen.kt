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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.js.movietrends.R
import com.js.movietrends.presentation.components.MovieListStaggeredGrid

@Composable
fun NowPlayingScreen(viewModel: NowPlayingViewModel = hiltViewModel()) {
    val nowPlayingMovies = viewModel.nowPlayingMovies.collectAsLazyPagingItems()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val refreshState = nowPlayingMovies.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator()
            }

            is LoadState.Error -> {
                if (nowPlayingMovies.itemCount > 0) {
                    MovieListStaggeredGrid(movies = nowPlayingMovies) // 캐시된 데이터
                }

                Text(
                    text = refreshState.error.localizedMessage
                        ?: stringResource(id = R.string.error_unknown),
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