package com.js.movietrends.data.datasource

import android.util.Log
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.data.util.ErrorMessages

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
                response.body() ?: throw Exception(ErrorMessages.EMPTY_RESPONSE_BODY)
            } else {
                throw Exception(String.format(ErrorMessages.API_CALL_FAILED, response.message()))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: ErrorMessages.UNEXPECTED_ERROR, e)
            throw e
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): MovieListResponseDto {
        return try {
            val response = movieApi.getNowPlayingMovies(
                page = page
            )
            if (response.isSuccessful) {
                response.body() ?: throw Exception(ErrorMessages.EMPTY_RESPONSE_BODY)
            } else {
                throw Exception(String.format(ErrorMessages.API_CALL_FAILED, response.message()))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: ErrorMessages.UNEXPECTED_ERROR, e)
            throw e
        }
    }

    override suspend fun getUpcomingMovies(page: Int): MovieListResponseDto {
        return try {
            val response = movieApi.getUpcomingMovies(
                page = page
            )
            if (response.isSuccessful) {
                response.body() ?: throw Exception(ErrorMessages.EMPTY_RESPONSE_BODY)
            } else {
                throw Exception(String.format(ErrorMessages.API_CALL_FAILED, response.message()))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: ErrorMessages.UNEXPECTED_ERROR, e)
            throw e
        }
    }
}
