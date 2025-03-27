package com.js.movietrends.ui.home.weeklyspotlight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.js.movietrends.ui.components.MovieTrendsButton
import com.js.movietrends.ui.theme.MovieTrendsTheme
import kotlinx.coroutines.delay

@Composable
fun WeeklySpotlightScreen(
    modifier: Modifier = Modifier,
    viewModel: WeeklySpotlightViewModel = hiltViewModel(),
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    val weeklySpotlightUiState: State<ApiResult<Movie>> =
        viewModel.weeklySpotlightUiState.collectAsStateWithLifecycle()
    WeeklySpotlightScreen(
        modifier = modifier,
        weeklySpotlightUiState = weeklySpotlightUiState,
        onNavigationToMovieDetail = onNavigationToMovieDetail
    )
}

@Composable
fun WeeklySpotlightScreen() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        visible = true
    }

    if (visible) {
        Text("텍스트")
    }
}

/**
 * state hoisting 적용한 screen composable
 */
@Composable
fun WeeklySpotlightScreen(
    modifier: Modifier = Modifier,
    weeklySpotlightUiState: State<ApiResult<Movie>>,
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        when (val apiResult = weeklySpotlightUiState.value) {
            is ApiResult.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is ApiResult.Error -> {
                // todo - 커스텀 텍스트 컴포넌트 대체 필요
                Text(
                    text = apiResult.exception.message
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
                val movieData = apiResult.data
                WeeklySpotlightContent(
                    modifier = Modifier.fillMaxSize(),
                    movie = movieData,
                    isImageLoaded = isImageLoaded,
                    onImageLoaded = {
                        isImageLoaded = it
                    },
                    onNavigationToMovieDetail = {
                        onNavigationToMovieDetail(movieData)
                    },
                )
            }
        }
    }
}

@Composable
fun WeeklySpotlightContent(
    modifier: Modifier = Modifier,
    movie: Movie,
    isImageLoaded: Boolean,
    onImageLoaded: (Boolean) -> Unit,
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    Box(
        modifier = modifier,
    ) {
        SubcomposeAsyncImage(
            model = movie.posterPath
                ?.let { "${Constants.POSTER_URL}${Constants.POSTER_FULL}$it" },
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier.background(Color.LightGray)
                )
            },
            error = { error ->
                Box(
                    modifier = Modifier.background(Color.Black)
                )
                error.result.throwable.message?.let {
                    Text(it)
                }
                onImageLoaded(false)
            },
            onSuccess = {
                onImageLoaded(true)
            }
        )

        // 이미지 로드 완료 후 차트 노출
        if (isImageLoaded) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedDonutChart(
                    modifier = Modifier.size(200.dp),
                    value = (movie.voteAverage ?: 0).toFloat(),
                    maxValue = 10,
                    durationMillis = 1000
                )
                Spacer(modifier = Modifier.height(20.dp))
                MovieTrendsButton(
                    onClick = {
                        onNavigationToMovieDetail(movie)
                    }) {
                    Text(
                        text = stringResource(id = R.string.details),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklySpotlightScreenContentPreview() {
    MovieTrendsTheme {
        WeeklySpotlightContent(
            movie = SampleData.createDummyMovie(),
            isImageLoaded = true,
            onImageLoaded = {},
            onNavigationToMovieDetail = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklySpotlightScreenErrorPreview() {
    MovieTrendsTheme {
        WeeklySpotlightScreen(
            modifier = Modifier.fillMaxSize(),
            weeklySpotlightUiState = remember { mutableStateOf(ApiResult.Error(Exception("Error Message"))) },
            onNavigationToMovieDetail = {},
        )
    }
}