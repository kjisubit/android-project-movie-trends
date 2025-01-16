package com.js.movietrends.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import coil3.compose.AsyncImage
import com.js.movietrends.domain.model.Movie

@Composable
fun MovieListColumn(movies: LazyPagingItems<Movie>) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)) {
        items(count = movies.itemCount) { index ->
            val movie = movies[index]
            movie?.let {
                MovieListColumnItem(movie)
            }
        }
    }
}


@Composable
fun MovieListColumnItem(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
            contentDescription = "Movie Poster",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(100.dp)),
            contentScale = ContentScale.FillWidth
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