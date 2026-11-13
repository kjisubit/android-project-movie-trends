package com.js.movietrends.data.util

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class QueryInterceptorTest {

    private fun intercept(interceptor: QueryInterceptor, url: String): Request {
        val chain = mock<Interceptor.Chain>()
        val original = Request.Builder().url(url).build()
        whenever(chain.request()).thenReturn(original)
        whenever(chain.proceed(any())).thenAnswer {
            Response.Builder()
                .request(it.getArgument(0))
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }

        interceptor.intercept(chain)

        val captor = argumentCaptor<Request>()
        verify(chain).proceed(captor.capture())
        return captor.firstValue
    }

    @Test
    fun intercept_addsApiKeyAndLanguage_keepsExistingQuery() {
        val interceptor = QueryInterceptor(apiKey = "test-key", languageProvider = { "ko-KR" })

        val request = intercept(interceptor, "https://api.themoviedb.org/3/movie/upcoming?page=2")

        assertEquals("test-key", request.url.queryParameter("api_key"))
        assertEquals("ko-KR", request.url.queryParameter("language"))
        assertEquals("2", request.url.queryParameter("page"))
    }

    @Test
    fun intercept_readsLanguageOnEveryRequest() {
        var language = "ko-KR"
        val interceptor = QueryInterceptor(apiKey = "test-key", languageProvider = { language })

        val first = intercept(interceptor, "https://api.themoviedb.org/3/movie/upcoming")
        language = "en-US"
        val second = intercept(interceptor, "https://api.themoviedb.org/3/movie/upcoming")

        assertEquals("ko-KR", first.url.queryParameter("language"))
        assertEquals("en-US", second.url.queryParameter("language"))
    }
}
