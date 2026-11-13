package com.js.movietrends.data.util

import com.js.movietrends.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

/**
 * 모든 TMDB 요청에 공통 쿼리 파라미터(api_key, language)를 붙인다.
 *
 * @param languageProvider 요청할 때마다 호출해 실행 중 언어 변경도 반영한다. (예: ko-KR)
 */
class QueryInterceptor(
    private val apiKey: String = BuildConfig.TMDB_API_KEY,
    private val languageProvider: () -> String = { Locale.getDefault().toLanguageTag() },
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter("api_key", apiKey)
            .addQueryParameter("language", languageProvider())
            .build()
        return chain.proceed(request.newBuilder().url(url).build())
    }
}
