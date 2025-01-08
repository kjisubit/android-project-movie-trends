package com.js.movietrends.data.datasource

import androidx.paging.PagingData
import com.js.movietrends.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getPopularMovies(): Flow<PagingData<Movie>>
}