package com.js.movietrends.data.datasource

import android.util.Log
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.dto.MovieListResponseDto
import retrofit2.HttpException

class RemoteDataSourceImpl(private val movieApi: MovieApi) : RemoteDataSource {

    companion object {
        private const val TAG = "RemoteDataSourceImpl"
    }

    override suspend fun getDiscoveredMovies(
        startDate: String,
        endDate: String,
        sortBy: String
    ): MovieListResponseDto {
        return try {
            val response = movieApi.getDiscoveredMovies(
                page = 1,
                primaryReleaseDateGte = startDate,
                primaryReleaseDateLte = endDate,
                sortBy = sortBy
            )
            if (response.isSuccessful) {
                response.body() ?: throw IllegalStateException("Empty response body")
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            Log.e(TAG, "API call failed", e)
            throw e
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): MovieListResponseDto {
        return try {
            val response = movieApi.getNowPlayingMovies(
                page = page
            )
            if (response.isSuccessful) {
                response.body() ?: throw IllegalStateException("Empty response body")
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            Log.e(TAG, "API call failed", e)
            throw e
        }
    }

    override suspend fun getUpcomingMovies(page: Int): MovieListResponseDto {
        return try {
            val response = movieApi.getUpcomingMovies(
                page = page
            )
            if (response.isSuccessful) {
                response.body() ?: throw IllegalStateException("Empty response body")
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            Log.e(TAG, "API call failed", e)
            throw e
        }
    }
}
