package com.js.movietrends.ui.home.weeklyspotlight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.js.movietrends.R
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie

@Composable
fun WeeklySpotlightScreen(
    viewModel: WeeklySpotlightViewModel = hiltViewModel(),
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    val weeklySpotlightUiState = viewModel.weeklySpotlightUiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when (val result = weeklySpotlightUiState.value) {
            is ApiResult.Loading -> {
                CircularProgressIndicator()
            }

            is ApiResult.Error -> {
                Text(
                    text = result.exception.message
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

            is ApiResult.Success -> {
                val movieData = result.data
                WeeklySpotlightContent(
                    movie = movieData,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onNavigationToMovieDetail(movieData)
                        },
                )
            }
        }
    }
}

@Composable
fun WeeklySpotlightContent(movie: Movie, modifier: Modifier = Modifier) {
    var imageLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = movie.posterPath
                ?.let { "${Constants.POSTER_URL}${Constants.POSTER_FULL}$it" },
            contentDescription = movie.title,
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
            },
            error = { error ->
                error.result.throwable.message?.let { Text(it) }
            },
            onSuccess = {
                imageLoaded = true
            }
        )

        if (imageLoaded) {
            AnimatedDonutChart(
                value = (movie.voteAverage ?: 0).toFloat(),
                maxValue = 10,
                modifier = Modifier.size(200.dp),
                durationMillis = 1000
            )
        }
    }
}