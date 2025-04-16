package com.js.movietrends.data.datasource

import android.util.Log
import com.js.movietrends.data.BuildConfig
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.data.util.ErrorMessages
import com.js.movietrends.domain.core.AppInfoManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RemoteDataSourceImpl(private val movieApi: MovieApi) : RemoteDataSource {

    companion object {
        private const val TAG = "RemoteDataSourceImpl"
    }

    override suspend fun getWeeklySpotlightedMovie(): MovieListResponseDto {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val oneWeekAgo = LocalDate.now().minusWeeks(1).format(formatter)
            val twoWeeksAgo = LocalDate.now().minusWeeks(2).format(formatter)

            val response = movieApi.getDiscoveredMovies(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = AppInfoManager.localeCode,
                page = 1,
                primaryReleaseDateGte = twoWeeksAgo,
                primaryReleaseDateLte = oneWeekAgo,
            )

            if (response.isSuccessful) {
                response.body() ?: throw Exception(ErrorMessages.EMPTY_RESPONSE_BODY)
            } else {
                throw Exception(String.format(ErrorMessages.API_CALL_FAILED, response.message()))
            }
        } catch (e: Exception) {
            // TODO: 응답 코드에 따른 예외처리 추가하기 위해, Throwable 상속한 sealed class 필요 (예: 401 → Unauthorized)
            Log.e(TAG, e.message ?: ErrorMessages.UNEXPECTED_ERROR, e)
            throw e
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): MovieListResponseDto {
        return try {
            val response = movieApi.getNowPlayingMovies(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = AppInfoManager.localeCode,
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
                apiKey = BuildConfig.TMDB_API_KEY,
                language = AppInfoManager.localeCode,
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
