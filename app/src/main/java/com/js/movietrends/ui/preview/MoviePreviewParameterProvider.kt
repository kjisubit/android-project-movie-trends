package com.js.movietrends.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.js.movietrends.domain.model.Movie

fun previewMovie(
    id: Int = 0,
    title: String = "Sample Movie",
    overview: String? = null,
    posterPath: String? = null,
    popularity: Double? = null,
    voteAverage: Double? = null,
    voteCount: Int? = null
) = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    popularity = popularity,
    voteAverage = voteAverage,
    voteCount = voteCount
)

class MoviePreviewParameterProvider : PreviewParameterProvider<Movie> {
    override val values = sequenceOf(
        previewMovie(id = 1, overview = "This is a sample overview for preview purposes.", voteAverage = 8.0, voteCount = 1000)
    )
}
