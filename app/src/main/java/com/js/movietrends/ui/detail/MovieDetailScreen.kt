package com.js.movietrends.ui.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.js.movietrends.R
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.model.SampleData
import com.js.movietrends.ui.components.MovieTrendsScaffold
import com.js.movietrends.ui.components.MovieTrendsSurface
import com.js.movietrends.ui.theme.MovieTrendsTheme
import com.js.movietrends.ui.utils.FormatUtil

@Composable
fun MovieDetailScreen(
    movie: Movie,
    upPress: () -> Unit
) {
    val scrollState = rememberScrollState()
    MovieDetailScreen(
        movie = movie,
        scrollState = scrollState,
        upPress = upPress
    )
}

/**
 * state hoisting 적용한 screen composable
 */
@Composable
fun MovieDetailScreen(
    movie: Movie,
    scrollState: ScrollState,
    upPress: () -> Unit
) {
    MovieTrendsScaffold { paddingValues ->
        MovieDetailContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            movie = movie,
            scrollState = scrollState,
            upPress = upPress
        )
    }
}

@Composable
fun MovieDetailContent(
    modifier: Modifier = Modifier,
    movie: Movie,
    scrollState: ScrollState,
    upPress: () -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        Header(
            movie = movie,
            upPress = upPress
        )
        Spacer(modifier = Modifier.height(10.dp))
        Body(movie = movie)
    }
}

@Composable
private fun Header(
    movie: Movie,
    upPress: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
    ) {
        SubcomposeAsyncImage(
            model = movie.posterPath?.let { "${Constants.POSTER_URL}${Constants.POSTER_XXLARGE}$it" },
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
            }
        )
        BackButton(
            modifier = Modifier
                .testTag("back_button")
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .size(36.dp)
                .background(
                    color = MovieTrendsTheme.colors.uiBackground.copy(alpha = 0.32f),
                    shape = CircleShape
                ),
            upPress = upPress
        )
    }
}

@Composable
private fun Body(movie: Movie) {
    MovieTrendsSurface {
        Column {
            Text(
                text = movie.title ?: "Unknown Title",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "★ ${FormatUtil.formatToOneDecimal(movie.voteAverage ?: 0.0)}/10",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = movie.overview ?: stringResource(R.string.no_description_available),
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )
        }
    }
}


@Composable
private fun BackButton(modifier: Modifier = Modifier, upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            tint = MovieTrendsTheme.colors.onBrand,
            contentDescription = stringResource(R.string.label_back)
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun MovieDetailScreenPreview() {
    MovieTrendsTheme {
        MovieDetailScreen(
            movie = SampleData.createDummyMovie(),
            upPress = {}
        )
    }
}
