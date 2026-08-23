package com.js.movietrends.data.paging

import androidx.paging.PagingSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.data.dto.MovieResponseDto
import com.js.movietrends.data.paging.pagingsource.UpcomingMoviePagingSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class UpcomingMoviePagingSourceTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var pagingSource: UpcomingMoviePagingSource

    @Before
    fun setUp() {
        pagingSource = UpcomingMoviePagingSource(remoteDataSource)
    }

    private fun movieResponse(page: Int, totalPages: Int, size: Int = 2) = MovieListResponseDto(
        page = page,
        totalPages = totalPages,
        results = List(size) { MovieResponseDto(id = it + 1) }
    )

    private fun refreshParams(key: Int?) = PagingSource.LoadParams.Refresh(
        key = key,
        loadSize = 20,
        placeholdersEnabled = false
    )

    @Test
    fun load_firstPage_prevKeyIsNull() = runTest {
        whenever(remoteDataSource.getUpcomingMovies(1)).thenReturn(movieResponse(1, 3))

        val result = pagingSource.load(refreshParams(key = null)) as PagingSource.LoadResult.Page

        assertNull(result.prevKey)
        assertEquals(2, result.nextKey)
    }

    @Test
    fun load_lastPage_nextKeyIsNull() = runTest {
        whenever(remoteDataSource.getUpcomingMovies(3)).thenReturn(movieResponse(3, 3))

        val result = pagingSource.load(refreshParams(key = 3)) as PagingSource.LoadResult.Page

        assertNull(result.nextKey)
        assertEquals(2, result.prevKey)
    }

    @Test
    fun load_emptyResults_nextKeyIsNull() = runTest {
        whenever(remoteDataSource.getUpcomingMovies(1)).thenReturn(
            MovieListResponseDto(page = 1, totalPages = 1, results = emptyList())
        )

        val result = pagingSource.load(refreshParams(key = null)) as PagingSource.LoadResult.Page

        assertNull(result.nextKey)
    }

    @Test
    fun load_middlePage_hasBothKeys() = runTest {
        whenever(remoteDataSource.getUpcomingMovies(2)).thenReturn(movieResponse(2, 3))

        val result = pagingSource.load(refreshParams(key = 2)) as PagingSource.LoadResult.Page

        assertEquals(1, result.prevKey)
        assertEquals(3, result.nextKey)
    }

    @Test
    fun load_apiError_returnsError() = runTest {
        whenever(remoteDataSource.getUpcomingMovies(1)).thenThrow(RuntimeException("Network error"))

        val result = pagingSource.load(refreshParams(key = null))

        assertTrue(result is PagingSource.LoadResult.Error)
    }
}
