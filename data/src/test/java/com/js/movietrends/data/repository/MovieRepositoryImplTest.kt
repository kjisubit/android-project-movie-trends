package com.js.movietrends.data.repository

import com.js.movietrends.data.database.entity.WeeklySpotlightEntity
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.data.dto.MovieResponseDto
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.ErrorType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    private fun response(vararg ids: Int) = MovieListResponseDto(
        page = 1,
        totalPages = 1,
        results = ids.map { MovieResponseDto(id = it, title = "Movie $it") }
    )

    private suspend fun load() = repository.getWeeklySpotlight(startDate, endDate, sortBy)

    @Test
    fun getWeeklySpotlight_cacheHit_returnsCacheWithoutApiCall() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(entity(queryKey, id = 1))

        val result = load()

        assertEquals(1, (result as ApiResult.Success).data.id)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun getWeeklySpotlight_cacheMiss_savesAndReturnsFetchedMovie() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy)).thenReturn(response(2))

        val result = load()

        verify(localDataSource).replaceWeeklySpotlight(argThat { this.queryKey == this@MovieRepositoryImplTest.queryKey && id == 2 })
        assertEquals(2, (result as ApiResult.Success).data.id)
    }

    @Test
    fun getWeeklySpotlight_staleCache_refetchesAndReplacesCache() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(entity("old|query|key", id = 1))
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy)).thenReturn(response(2))

        val result = load()

        verify(localDataSource).replaceWeeklySpotlight(argThat { this.queryKey == this@MovieRepositoryImplTest.queryKey && id == 2 })
        assertEquals(2, (result as ApiResult.Success).data.id)
    }

    @Test
    fun getWeeklySpotlight_apiErrorWithStaleCache_returnsStaleCache() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(entity("old|query|key", id = 1))
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenThrow(RuntimeException("Network error"))

        val result = load()

        assertEquals(1, (result as ApiResult.Success).data.id)
        verify(localDataSource, never()).replaceWeeklySpotlight(any())
    }

    @Test
    fun getWeeklySpotlight_apiErrorWithoutCache_returnsMappedError() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenAnswer { throw IOException("Network error") }

        val result = load()

        assertEquals(ApiResult.Error(ErrorType.NETWORK), result)
    }

    @Test
    fun getWeeklySpotlight_emptyResults_returnsNoMovieError() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy)).thenReturn(response())

        val result = load()

        assertEquals(ApiResult.Error(ErrorType.NO_MOVIE), result)
        verify(localDataSource, never()).replaceWeeklySpotlight(any())
    }

    @Test
    fun getWeeklySpotlight_cacheReadFails_returnsErrorInsteadOfThrowing() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenThrow(RuntimeException("DB read error"))

        val result = load()

        assertEquals(ApiResult.Error(ErrorType.UNKNOWN), result)
    }

    @Test
    fun getWeeklySpotlight_cacheSaveFails_returnsFetchedMovie() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy)).thenReturn(response(2))
        whenever(localDataSource.replaceWeeklySpotlight(any())).thenThrow(RuntimeException("DB write error"))

        val result = load()

        assertEquals(2, (result as ApiResult.Success).data.id)
    }

    @Test
    fun getWeeklySpotlight_cancelled_rethrowsCancellation() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(entity("old|query|key", id = 1))
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenThrow(CancellationException("Cancelled"))

        val thrown = try {
            load()
            null
        } catch (e: CancellationException) {
            e
        }

        assertTrue(thrown is CancellationException)
    }
}
