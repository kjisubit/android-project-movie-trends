package com.js.movietrends.ui.home.upcoming

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import com.js.movietrends.ui.components.MovieListCell
import com.js.movietrends.ui.theme.MovieTrendsTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

@Composable
fun UpcomingScreen(
    modifier: Modifier = Modifier,
    viewModel: UpcomingViewModel = hiltViewModel(),
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    val upcomingPagingItems = viewModel.upcomingUiState.collectAsLazyPagingItems()
    UpcomingScreen(
        modifier = modifier,
        upcomingPagingItems = upcomingPagingItems,
        onNavigationToMovieDetail = onNavigationToMovieDetail
    )
}

@Composable
fun UpcomingScreen(
    modifier: Modifier = Modifier,
    upcomingPagingItems: LazyPagingItems<Movie>,
    onNavigationToMovieDetail: (Movie) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val refreshState = upcomingPagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is LoadState.Error -> {
                // api 호출 실패 시, 캐시 아이템 노출
                if (upcomingPagingItems.itemCount > 0) {
                    MovieListColumn(
                        movies = upcomingPagingItems,
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
                MovieListColumn(
                    movies = upcomingPagingItems,
                    onItemClick = { movie ->
                        onNavigationToMovieDetail(movie)
                    }
                )
            }
        }
    }
}

@Composable
fun MovieListColumn(
    movies: LazyPagingItems<Movie>,
    modifier: Modifier = Modifier,
    onItemClick: (Movie) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 10.dp
        ),
        modifier = modifier.testTag("upcoming:movies")
    ) {
        items(count = movies.itemCount) { index ->
            val movie = movies[index]
            movie?.let {
                MovieListCell(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
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
fun UpcomingScreenContentPreview() {
    val size = 20
    val fakeMovies = List(size) {
        SampleData.createDummyMovie(id = Random.nextInt(0, size))
    }
    val upcomingUiState = MutableStateFlow(
        PagingData.from(fakeMovies)
    )

    MovieTrendsTheme {
        UpcomingScreen(
            modifier = Modifier.fillMaxSize(),
            upcomingPagingItems = upcomingUiState.collectAsLazyPagingItems(),
            onNavigationToMovieDetail = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UpcomingScreenErrorPreview() {
    val fakeMovies = List(0) { index ->
        SampleData.createDummyMovie(id = index)
    }
    val upcomingUiState = MutableStateFlow(
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
                )
            )
        )
    )

    MovieTrendsTheme {
        UpcomingScreen(
            modifier = Modifier.fillMaxSize(),
            upcomingPagingItems = upcomingUiState.collectAsLazyPagingItems(),
            onNavigationToMovieDetail = {}
        )
    }
}