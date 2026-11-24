package com.js.movietrends.data.mapper

import com.js.movietrends.data.database.entity.NowPlayingMovieEntity
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

    fun mapNowPlayingMovieEntityToDomain(nowPlayingMovieEntity: NowPlayingMovieEntity): Movie {
        return Movie(
            id = nowPlayingMovieEntity.id,
            posterPath = nowPlayingMovieEntity.posterPath,
            title = nowPlayingMovieEntity.title,
            overview = nowPlayingMovieEntity.overview,
            popularity = nowPlayingMovieEntity.popularity,
            voteAverage = nowPlayingMovieEntity.voteAverage,
            voteCount = nowPlayingMovieEntity.voteCount
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
