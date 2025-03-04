package com.js.movietrends.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.model.SampleData
import com.js.movietrends.ui.theme.MovieTrendsTheme

@Composable
fun MovieGridCell(
    movie: Movie,
    onItemClick: (Movie) -> Unit
) {
    // 영화 id 값으로 포스터 이미지 화면비 결정
    val movieId = (movie.id ?: 0)
    val aspectRatioVariant = 5 // 화면비 종류
    val minAspectRatio = 0.5
    val maxAspectRatio = 0.7
    val ratioInterval = (maxAspectRatio - minAspectRatio) / aspectRatioVariant
    val aspectRatio = (minAspectRatio + movieId % aspectRatioVariant * ratioInterval).toFloat()

    SubcomposeAsyncImage(
        model = movie.posterPath
            ?.let { "${Constants.POSTER_URL}${Constants.POSTER_XLARGE}$it" },
        contentDescription = movie.title,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clickable {
                onItemClick(movie)
            },
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
            error.result.throwable.message?.let { Text(it) }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MovieGridCellPreview() {
    MovieTrendsTheme {
        MovieGridCell(
            movie = SampleData.createDummyMovie(),
            onItemClick = {}
        )
    }
}
