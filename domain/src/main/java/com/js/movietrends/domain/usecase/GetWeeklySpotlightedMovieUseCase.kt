package com.js.movietrends.domain.usecase

import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GetWeeklySpotlightedMovieUseCase(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): Flow<ApiResult<Movie>> {
        return movieRepository.getWeeklySpotlightedMovie(
            startDate = LocalDate.now().minusWeeks(2)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            endDate = LocalDate.now().minusWeeks(1)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )
    }
}