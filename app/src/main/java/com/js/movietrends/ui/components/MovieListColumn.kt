package com.js.movietrends.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import coil3.compose.SubcomposeAsyncImage
import com.js.movietrends.domain.core.Constants
import com.js.movietrends.domain.model.Movie

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
        modifier = modifier
    ) {
        items(count = movies.itemCount) { index ->
            val movie = movies[index]
            movie?.let {
                MovieListColumnItem(movie, onItemClick)
            }
        }
    }
}

@Composable
fun MovieListColumnItem(
    movie: Movie,
    onItemClick: (Movie) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onItemClick(movie)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = movie.posterPath
                ?.let { "${Constants.POSTER_URL}${Constants.POSTER_XLARGE}$it" },
            contentDescription = movie.title,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(100.dp)),
            contentScale = ContentScale.FillWidth,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${movie.title}",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
    }
}