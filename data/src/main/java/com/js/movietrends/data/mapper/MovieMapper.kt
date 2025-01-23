package com.js.movietrends.data.mapper

import com.js.movietrends.data.model.MovieResponse
import com.js.movietrends.domain.model.Movie

object MovieMapper {
    fun mapToDomain(movieResponse: MovieResponse): Movie {
        return Movie(
            id = movieResponse.id,
            posterPath = movieResponse.posterPath,
            title = movieResponse.title
        )
    }
}