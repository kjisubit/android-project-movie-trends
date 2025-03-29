package com.js.movietrends.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.model.SampleData
import com.js.movietrends.ui.theme.MovieTrendsTheme

@Composable
fun MovieListCell(
    modifier: Modifier = Modifier,
    movie: Movie
) {
    MovieTrendsSurface(modifier = modifier.testTag("movieListItem:${movie.id}")) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 3.dp)
        ) {
            SubcomposeAsyncImage(
                model = movie.posterPath?.let { "${Constants.POSTER_URL}${Constants.POSTER_XLARGE}$it" },
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(100.dp)),
                contentScale = ContentScale.FillWidth,
                loading = {
                    Box(modifier = Modifier.background(Color.LightGray))
                },
                error = { error ->
                    Box(modifier = Modifier.background(Color.Black))
                    error.result.throwable.message?.let {
                        Text(it)
                    }
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${movie.title}",
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview("default")
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun MovieListCellPreview() {
    MovieTrendsTheme {
        MovieListCell(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            movie = SampleData.createDummyMovie()
        )
    }
}
