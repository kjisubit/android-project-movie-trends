package com.js.movietrends.domain.usecase

import com.js.movietrends.domain.repository.MovieRepository

class GetUpcomingMoviesUseCase(private val movieRepository: MovieRepository) {
    operator fun invoke() = movieRepository.getUpcomingMovies()
}