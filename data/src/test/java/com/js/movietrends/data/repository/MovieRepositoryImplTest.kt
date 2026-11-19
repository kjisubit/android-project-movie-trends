package com.js.movietrends.data.repository

import com.js.movietrends.data.database.entity.WeeklySpotlightEntity
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.data.dto.MovieResponseDto
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.ErrorType
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryImplTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    private lateinit var repository: MovieRepositoryImpl

    private val startDate = "2026-10-20"
    private val endDate = "2026-10-27"
    private val sortBy = "popularity.desc"
    private val queryKey = "$startDate|$endDate|$sortBy"

    @Before
    fun setUp() {
        repository = MovieRepositoryImpl(remoteDataSource, localDataSource)
    }

    private fun entity(queryKey: String, id: Int) = WeeklySpotlightEntity(
        queryKey = queryKey,
        id = id,
        overview = null,
        popularity = null,
        posterPath = null,
        title = "Movie $id",
        voteAverage = null,
        voteCount = null,
        cachedAt = 1000L
    )

    private suspend fun collect() =
        repository.getWeeklySpotlight(startDate, endDate, sortBy).toList()

    @Test
    fun getWeeklySpotlight_cacheHit_returnsCacheWithoutApiCall() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(entity(queryKey, id = 1))

        val result = collect()

        assertEquals(1, result.size)
        assertEquals(1, (result[0] as ApiResult.Success).data.id)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun getWeeklySpotlight_cacheMiss_fetchesSavesAndReturnsFromDb() = runTest {
        whenever(localDataSource.getWeeklySpotlight())
            .thenReturn(null)
            .thenReturn(entity(queryKey, id = 2))
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenReturn(
                MovieListResponseDto(
                    page = 1,
                    totalPages = 1,
                    results = listOf(MovieResponseDto(id = 2))
                )
            )

        val result = collect()

        verify(localDataSource).replaceWeeklySpotlight(argThat { this.queryKey == this@MovieRepositoryImplTest.queryKey && id == 2 })
        assertEquals(2, (result.single() as ApiResult.Success).data.id)
    }

    @Test
    fun getWeeklySpotlight_staleCache_refetchesWithNewQuery() = runTest {
        val stale = entity("old|query|key", id = 1)
        whenever(localDataSource.getWeeklySpotlight())
            .thenReturn(stale)
            .thenReturn(entity(queryKey, id = 2))
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenReturn(
                MovieListResponseDto(
                    page = 1,
                    totalPages = 1,
                    results = listOf(MovieResponseDto(id = 2))
                )
            )

        val result = collect()

        verify(remoteDataSource).getDiscoveredMovies(startDate, endDate, sortBy)
        assertEquals(2, (result.single() as ApiResult.Success).data.id)
    }

    @Test
    fun getWeeklySpotlight_apiErrorWithStaleCache_returnsStaleCache() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(entity("old|query|key", id = 1))
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenThrow(RuntimeException("Network error"))

        val result = collect()

        assertEquals(1, (result.single() as ApiResult.Success).data.id)
        verify(localDataSource, never()).replaceWeeklySpotlight(any())
    }

    @Test
    fun getWeeklySpotlight_networkErrorWithoutCache_returnsNetworkError() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenAnswer { throw IOException("Network error") }

        val result = collect()

        assertEquals(ApiResult.Error(ErrorType.NETWORK), result.single())
    }

    @Test
    fun getWeeklySpotlight_httpErrorWithoutCache_returnsServerError() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenThrow(HttpException(Response.error<Any>(500, "".toResponseBody())))

        val result = collect()

        assertEquals(ApiResult.Error(ErrorType.SERVER), result.single())
    }

    @Test
    fun getWeeklySpotlight_unexpectedErrorWithoutCache_returnsUnknownError() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenThrow(RuntimeException("Unexpected"))

        val result = collect()

        assertEquals(ApiResult.Error(ErrorType.UNKNOWN), result.single())
    }

    @Test
    fun getWeeklySpotlight_emptyResults_returnsNoMovieError() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenReturn(MovieListResponseDto(page = 1, totalPages = 1, results = emptyList()))

        val result = collect()

        assertEquals(ApiResult.Error(ErrorType.NO_MOVIE), result.single())
        verify(localDataSource, never()).replaceWeeklySpotlight(any())
    }
}
