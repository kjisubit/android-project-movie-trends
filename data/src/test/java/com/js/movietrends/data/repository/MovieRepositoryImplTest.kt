package com.js.movietrends.data.repository

import com.js.movietrends.data.database.entity.WeeklySpotlightEntity
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.data.dto.MovieResponseDto
import com.js.movietrends.domain.model.ApiResult
import kotlinx.coroutines.flow.toList
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
        repository.getDiscoveredMovies(startDate, endDate, sortBy).toList()

    @Test
    fun getDiscoveredMovies_cacheHit_returnsCacheWithoutApiCall() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(entity(queryKey, id = 1))

        val result = collect()

        assertEquals(1, result.size)
        assertEquals(1, (result[0] as ApiResult.Success).data.id)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun getDiscoveredMovies_cacheMiss_fetchesSavesAndReturnsFromDb() = runTest {
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
    fun getDiscoveredMovies_staleCache_refetchesWithNewQuery() = runTest {
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
    fun getDiscoveredMovies_apiErrorWithStaleCache_returnsStaleCache() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(entity("old|query|key", id = 1))
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenThrow(RuntimeException("Network error"))

        val result = collect()

        assertEquals(1, (result.single() as ApiResult.Success).data.id)
        verify(localDataSource, never()).replaceWeeklySpotlight(any())
    }

    @Test
    fun getDiscoveredMovies_apiErrorWithoutCache_returnsError() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenThrow(RuntimeException("Network error"))

        val result = collect()

        assertTrue(result.single() is ApiResult.Error)
    }

    @Test
    fun getDiscoveredMovies_emptyResults_returnsError() = runTest {
        whenever(localDataSource.getWeeklySpotlight()).thenReturn(null)
        whenever(remoteDataSource.getDiscoveredMovies(startDate, endDate, sortBy))
            .thenReturn(MovieListResponseDto(page = 1, totalPages = 1, results = emptyList()))

        val result = collect()

        val error = result.single() as ApiResult.Error
        assertTrue(error.exception is NoSuchElementException)
        verify(localDataSource, never()).replaceWeeklySpotlight(any())
    }
}
