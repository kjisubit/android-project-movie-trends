package com.js.movietrends.data.mapper

import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.dto.MovieResponseDto
import com.js.movietrends.domain.model.Movie

object ModelMapper {
    fun mapMovieResponseDtoToDomain(movieResponseDto: MovieResponseDto): Movie {
        return Movie(
            id = movieResponseDto.id,
            posterPath = movieResponseDto.posterPath,
            title = movieResponseDto.title,
            overview = movieResponseDto.overview,
            popularity = movieResponseDto.popularity,
            voteAverage = movieResponseDto.voteAverage,
            voteCount = movieResponseDto.voteCount
        )
    }

    fun mapMovieEntityToDomain(movieEntity: MovieEntity): Movie {
        return Movie(
            id = movieEntity.id,
            posterPath = movieEntity.posterPath,
            title = movieEntity.title,
            overview = movieEntity.overview,
            popularity = movieEntity.popularity,
            voteAverage = movieEntity.voteAverage,
            voteCount = movieEntity.voteCount
        )
    }
}