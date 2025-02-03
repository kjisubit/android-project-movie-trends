package com.js.movietrends.data.mapper

import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.model.MovieResponse
import com.js.movietrends.domain.model.Movie

object MovieMapper {
    fun mapDtoToDomain(movieResponse: MovieResponse): Movie {
        return Movie(
            id = movieResponse.id,
            posterPath = movieResponse.posterPath,
            title = movieResponse.title
        )
    }

    fun mapEntityToDomain(movieEntity: MovieEntity): Movie {
        return Movie(
            id = movieEntity.id,
            posterPath = movieEntity.posterPath,
            title = movieEntity.title
        )
    }
}