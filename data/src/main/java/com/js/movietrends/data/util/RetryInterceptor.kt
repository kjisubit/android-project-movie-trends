package com.js.movietrends.data.util

import okhttp3.Interceptor
import okhttp3.Response
import kotlin.math.pow

class RetryInterceptor(private val maxRetry: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        var response = chain.proceed(chain.request())
        while (attempt < maxRetry) {
            when (response.code) {
                in 500..599 -> {
                    response.close()
                    attempt++
                    Thread.sleep(2.0.pow(attempt).toLong() * 1000)
                }
                429 -> {
                    val retryAfter = response.header("Retry-After")?.toLongOrNull() ?: 2.0.pow(attempt).toLong()
                    response.close()
                    attempt++
                    Thread.sleep(retryAfter * 1000)
                }
                else -> return response
            }
            response = chain.proceed(chain.request())
        }
        return response
    }
}
