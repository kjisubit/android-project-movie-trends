package com.js.movietrends.data.datasource

import androidx.paging.PagingData
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.model.MovieResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getNowPlayingMovies(): Flow<PagingData<MovieEntity>>
    fun getTopRatedMovies(): Flow<PagingData<MovieResponse>>
    fun getUpcomingMovies(): Flow<PagingData<MovieResponse>>
}