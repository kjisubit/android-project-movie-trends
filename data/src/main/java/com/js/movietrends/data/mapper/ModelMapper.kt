package com.js.movietrends.data.mapper

import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.WeeklySpotlightEntity
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

    fun mapMovieResponseDtoToWeeklySpotlightEntity(
        movieResponseDto: MovieResponseDto,
        queryKey: String,
        cachedAt: Long,
    ): WeeklySpotlightEntity {
        return WeeklySpotlightEntity(
            queryKey = queryKey,
            id = movieResponseDto.id,
            posterPath = movieResponseDto.posterPath,
            title = movieResponseDto.title,
            overview = movieResponseDto.overview,
            popularity = movieResponseDto.popularity,
            voteAverage = movieResponseDto.voteAverage,
            voteCount = movieResponseDto.voteCount,
            cachedAt = cachedAt,
        )
    }

    fun mapWeeklySpotlightEntityToDomain(weeklySpotlightEntity: WeeklySpotlightEntity): Movie {
        return Movie(
            id = weeklySpotlightEntity.id,
            posterPath = weeklySpotlightEntity.posterPath,
            title = weeklySpotlightEntity.title,
            overview = weeklySpotlightEntity.overview,
            popularity = weeklySpotlightEntity.popularity,
            voteAverage = weeklySpotlightEntity.voteAverage,
            voteCount = weeklySpotlightEntity.voteCount
        )
    }
}
