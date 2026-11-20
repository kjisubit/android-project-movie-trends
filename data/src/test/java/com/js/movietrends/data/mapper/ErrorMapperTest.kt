package com.js.movietrends.data.mapper

import com.js.movietrends.domain.model.ErrorType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ErrorMapperTest {

    @Test
    fun toErrorType_ioException_returnsNetwork() {
        assertEquals(ErrorType.NETWORK, IOException().toErrorType())
    }

    @Test
    fun toErrorType_httpException_returnsServer() {
        val exception = HttpException(Response.error<Any>(500, "".toResponseBody()))

        assertEquals(ErrorType.SERVER, exception.toErrorType())
    }

    @Test
    fun toErrorType_noSuchElementException_returnsNoMovie() {
        assertEquals(ErrorType.NO_MOVIE, NoSuchElementException().toErrorType())
    }

    @Test
    fun toErrorType_otherException_returnsUnknown() {
        assertEquals(ErrorType.UNKNOWN, IllegalStateException().toErrorType())
    }
}
