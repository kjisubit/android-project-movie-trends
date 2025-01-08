package com.js.movietrends.domain.repository

import androidx.paging.PagingData
import com.js.movietrends.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMovies(): Flow<PagingData<Movie>>
}