package com.js.movietrends.data.utils

import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.model.MovieResponse
import com.js.movietrends.domain.model.Movie

object ModelMapper {
    fun mapMovieResponseToDomain(movieResponse: MovieResponse): Movie {
        return Movie(
            id = movieResponse.id,
            posterPath = movieResponse.posterPath,
            title = movieResponse.title
        )
    }

    fun mapMovieEntityToDomain(movieEntity: MovieEntity): Movie {
        return Movie(
            id = movieEntity.id,
            posterPath = movieEntity.posterPath,
            title = movieEntity.title
        )
    }
}