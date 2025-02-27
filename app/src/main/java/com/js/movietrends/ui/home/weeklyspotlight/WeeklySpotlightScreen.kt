package com.js.movietrends.ui.home.weeklyspotlight

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.js.movietrends.R
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.model.SampleData

@Composable
fun WeeklySpotlightScreen(
    modifier: Modifier = Modifier,
    viewModel: WeeklySpotlightViewModel = hiltViewModel(),
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    val weeklySpotlightUiState = viewModel.weeklySpotlightUiState.collectAsStateWithLifecycle()
    WeeklySpotlightScreen(
        modifier = modifier,
        stateApiResult = weeklySpotlightUiState,
        onNavigationToMovieDetail = onNavigationToMovieDetail
    )
}

@Composable
fun WeeklySpotlightScreen(
    modifier: Modifier = Modifier,
    stateApiResult: State<ApiResult<Movie>>,
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val result = stateApiResult.value) {
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
                    onNavigationToMovieDetail = {
                        onNavigationToMovieDetail(movieData)
                    },
                )
            }
        }
    }
}

@Composable
fun WeeklySpotlightContent(modifier: Modifier = Modifier, movie: Movie, onNavigationToMovieDetail: (Movie) -> Unit) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            model = movie.posterPath
                ?.let { "${Constants.POSTER_URL}${Constants.POSTER_FULL}$it" },
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = isImageLoaded) {
                    onNavigationToMovieDetail(movie)
                },
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.background(Color.LightGray)
                )
            },
            error = { error ->
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Default Image",
                    contentScale = ContentScale.Crop
                )
                error.result.throwable.message?.let { Text(it) }
            },
            onSuccess = {
                isImageLoaded = true
            }
        )

        AnimatedDonutChart(
            value = (movie.voteAverage ?: 0).toFloat(),
            maxValue = 10,
            modifier = Modifier.size(200.dp),
            durationMillis = 1000
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklySpotlightScreenPreview() {
    val previewState = remember {
        mutableStateOf(ApiResult.Success(SampleData.movie))
    }

    WeeklySpotlightScreen(
        stateApiResult = previewState,
        onNavigationToMovieDetail = {},
    )
}