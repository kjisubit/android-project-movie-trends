package com.js.movietrends.ui.home.nowplaying

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.js.movietrends.R
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.model.SampleData
import com.js.movietrends.ui.components.MovieGridCell
import com.js.movietrends.ui.theme.MovieTrendsTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

@Composable
fun NowPlayingScreen(
    modifier: Modifier = Modifier,
    viewModel: NowPlayingViewModel = hiltViewModel(),
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    val nowPlayingUiState = viewModel.nowPlayingUiState.collectAsLazyPagingItems()
    NowPlayingScreen(
        modifier = modifier,
        nowPlayingUiState = nowPlayingUiState,
        onNavigationToMovieDetail = onNavigationToMovieDetail
    )
}

/**
 * state hoisting 적용한 screen composable
 */
@Composable
fun NowPlayingScreen(
    modifier: Modifier = Modifier,
    nowPlayingUiState: LazyPagingItems<Movie>,
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val refreshState = nowPlayingUiState.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is LoadState.Error -> {
                if (nowPlayingUiState.itemCount > 0) { // 캐시에 저장된 데이터 노출
                    MovieListStaggeredGrid(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        movies = nowPlayingUiState,
                        onItemClick = { movie ->
                            onNavigationToMovieDetail(movie)
                        }
                    )
                }

                // todo - 커스텀 텍스트 컴포넌트 대체 필요
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
                MovieListStaggeredGrid(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    movies = nowPlayingUiState,
                    onItemClick = { movie ->
                        onNavigationToMovieDetail(movie)
                    }
                )
            }
        }
    }
}

@Composable
fun MovieListStaggeredGrid(
    modifier: Modifier = Modifier,
    movies: LazyPagingItems<Movie>,
    onItemClick: (Movie) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier,
    ) {
        items(count = movies.itemCount) { index ->
            /**
             * 영화 id 값으로 포스터 이미지 화면비 결정
             */
            val movie = movies[index]
            val movieId = (movie?.id ?: 0)
            val aspectRatioVariant = 5 // 화면비 종류
            val minAspectRatio = 0.5 // 최소 화면비
            val maxAspectRatio = 0.7 // 최대 화면비
            val ratioInterval = (maxAspectRatio - minAspectRatio) / aspectRatioVariant
            val aspectRatio =
                (minAspectRatio + movieId % aspectRatioVariant * ratioInterval).toFloat()

            movie?.let {
                MovieGridCell(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                        .clickable {
                            onItemClick(movie)
                        },
                    movie = movie
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NowPlayingScreenErrorPreview() {
    val size = 20
    val fakeMovies = List(size) {
        SampleData.createDummyMovie(id = Random.nextInt(0, size))
    }
    val nowPlayingUiState = MutableStateFlow(
        PagingData.from(fakeMovies)
    )

    MovieTrendsTheme {
        NowPlayingScreen(
            modifier = Modifier.fillMaxSize(),
            nowPlayingUiState = nowPlayingUiState.collectAsLazyPagingItems(),
            onNavigationToMovieDetail = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NowPlayingScreenContentPreview() {
    val fakeMovies = List(0) { index ->
        SampleData.createDummyMovie(id = index)
    }
    val nowPlayingUiState = MutableStateFlow(
        PagingData.from(
            fakeMovies,
            sourceLoadStates = LoadStates(
                refresh = LoadState.Error(
                    error = Throwable("Paging Failure")
                ),
                append = LoadState.Error(
                    error = Throwable("Paging Failure")
                ),
                prepend = LoadState.Error(
                    error = Throwable("Paging Failure")
                ),
            ),
        )
    )

    MovieTrendsTheme {
        NowPlayingScreen(
            modifier = Modifier.fillMaxSize(),
            nowPlayingUiState = nowPlayingUiState.collectAsLazyPagingItems(),
            onNavigationToMovieDetail = {},
        )
    }
}