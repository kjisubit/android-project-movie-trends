package com.js.movietrends.data.datasource

import android.util.Log
import com.js.movietrends.data.BuildConfig
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.data.utils.ApiResponseHandler
import com.js.movietrends.domain.core.AppInfoManager
import com.js.movietrends.domain.model.ApiResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RemoteDataSourceImpl(private val movieApi: MovieApi) : RemoteDataSource {

    companion object {
        private const val TAG = "RemoteDataSourceImpl"
    }

    override suspend fun getWeeklySpotlightedMovie(): ApiResult<MovieListResponseDto> {
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
            ApiResponseHandler.handleApiResponse(response)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "", e)
            ApiResult.Error(e)
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): ApiResult<MovieListResponseDto> {
        return try {
            val response = movieApi.getNowPlayingMovies(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = AppInfoManager.localeCode,
                page = page
            )
            ApiResponseHandler.handleApiResponse(response)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    override suspend fun getUpcomingMovies(page: Int): ApiResult<MovieListResponseDto> {
        return try {
            val response = movieApi.getUpcomingMovies(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = AppInfoManager.localeCode,
                page = page
            )
            ApiResponseHandler.handleApiResponse(response)
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "", e)
            ApiResult.Error(e)
        }
    }


}