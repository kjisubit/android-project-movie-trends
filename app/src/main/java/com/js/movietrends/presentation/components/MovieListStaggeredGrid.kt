package com.js.movietrends.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil3.compose.SubcomposeAsyncImage
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.domain.model.Movie

@Composable
fun MovieListStaggeredGrid(movies: LazyPagingItems<Movie>) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(count = movies.itemCount) { index ->
            val movie = movies[index]
            movie?.let {
                MovieListStaggeredGridItem(movie)
            }
        }
    }
}

@Composable
fun MovieListStaggeredGridItem(movie: Movie) {
    val posterUrl =
        movie.posterPath?.let { "${Constants.POSTER_URL}${Constants.POSTER_XXLARGE}$it" }

    // 영화 id 값으로 포스터 이미지 화면비 결정
    val movieId = (movie.id ?: 0)
    val aspectRatioVariant = 5 // 화면비 종류
    val minAspectRatio = 0.5
    val maxAspectRatio = 0.7
    val ratioInterval = (maxAspectRatio - minAspectRatio) / aspectRatioVariant
    val aspectRatio = (minAspectRatio + movieId % aspectRatioVariant * ratioInterval).toFloat()

    SubcomposeAsyncImage(
        model = posterUrl,
        contentDescription = "Movie Poster",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio),
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
    )

}