package com.js.movietrends.data.utils

import android.util.Log
import com.js.movietrends.domain.model.ApiResult
import retrofit2.Response

object ApiResponseHandler {
    private const val TAG = "ApiResponseHandler"

    fun <T> handleApiResponse(response: Response<T>): ApiResult<T> {
        if (!response.isSuccessful) {
            val errorMessage = response.message().ifEmpty { "Unknown Error" }
            Log.e(TAG, errorMessage)
            return ApiResult.Error(Throwable(errorMessage))
        }

        val body = response.body()
        if (body == null) {
            val errorMessage = "API call failed: Response body is empty."
            Log.e(TAG, errorMessage)
            return ApiResult.Error(Throwable(errorMessage))
        }

        return ApiResult.Success(body)
    }
}