package com.js.movietrends.domain.usecase

import com.js.movietrends.domain.repository.MovieRepository

class GetTopRatedMoviesUseCase(private val movieRepository: MovieRepository) {
    operator fun invoke() = movieRepository.getTopRatedMovies()
}