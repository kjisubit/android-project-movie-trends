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
    modifier: Modifier = Modifier,
    movie: Movie,
) {
    SubcomposeAsyncImage(
        model = movie.posterPath
            ?.let { "${Constants.POSTER_URL}${Constants.POSTER_XLARGE}$it" },
        contentDescription = movie.title,
        modifier = modifier,
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
        val movieId = (0)
        val aspectRatioVariant = 5 // 화면비 종류
        val minAspectRatio = 0.5 // 최소 화면비
        val maxAspectRatio = 0.7 // 최대 화면비
        val ratioInterval = (maxAspectRatio - minAspectRatio) / aspectRatioVariant
        val aspectRatio =
            (minAspectRatio + movieId % aspectRatioVariant * ratioInterval).toFloat()

        MovieGridCell(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clickable {
                },
            movie = SampleData.createDummyMovie(),
        )
    }
}
