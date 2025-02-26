package com.js.movietrends.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.js.movietrends.R
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.ui.utils.FormatUtil

@Composable
fun MovieDetailScreen(movie: Movie) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        if (!movie.posterPath.isNullOrEmpty()) {
            SubcomposeAsyncImage(model = movie.posterPath?.let { "${Constants.POSTER_URL}${Constants.POSTER_XXLARGE}$it" },
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
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
                })
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // 로컬 디폴트 이미지 리소스 ID
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.FillWidth
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            text = movie.title ?: "Unknown Title",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            text = "★ ${FormatUtil.formatToOneDecimal(movie.voteAverage ?: 0.0)}/10",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            text = movie.overview ?: "No description available.",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DetailPreview() {
//    val movie = Movie(
//        id = 123,
//        title = "Title",
//        posterPath = "https://image.tmdb.org/t/p/w500/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg",
//        voteAverage = 8.9,
//        overview = "Overview",
//        popularity = 2.2,
//        voteCount = 3
//    )
//    MovieDetailScreen(movie = movie)
//}