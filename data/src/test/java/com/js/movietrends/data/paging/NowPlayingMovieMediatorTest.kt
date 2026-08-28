package com.js.movietrends.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.RemoteMediator.MediatorResult
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.dto.MovieListResponseDto
import com.js.movietrends.data.dto.MovieResponseDto
import com.js.movietrends.data.paging.remotemediator.NowPlayingMovieMediator
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

@OptIn(ExperimentalPagingApi::class)
@RunWith(MockitoJUnitRunner::class)
class NowPlayingMovieMediatorTest {

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var localDataSource: LocalDataSource

    private lateinit var mediator: NowPlayingMovieMediator

    @Before
    fun setUp() {
        mediator = NowPlayingMovieMediator(remoteDataSource, localDataSource)
    }

    private fun emptyPagingState() = PagingState<Int, MovieEntity>(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 20),
        leadingPlaceholderCount = 0
    )

    private fun pagingStateWithLastItem(movieId: Int) = PagingState(
        pages = listOf(
            PagingSource.LoadResult.Page(
                data = listOf(MovieEntity(id = movieId, title = null, overview = null, popularity = null, posterPath = null, voteAverage = null, voteCount = null)),
                prevKey = null,
                nextKey = 2
            )
        ),
        anchorPosition = 0,
        config = PagingConfig(pageSize = 20),
        leadingPlaceholderCount = 0
    )

    private fun movieResponse(page: Int, totalPages: Int, size: Int = 2) = MovieListResponseDto(
        page = page,
        totalPages = totalPages,
        results = List(size) { MovieResponseDto(id = it + 1) }
    )

    @Test
    fun load_refresh_requestsFirstPage() = runTest {
        whenever(remoteDataSource.getNowPlayingMovies(1)).thenReturn(movieResponse(1, 3))

        val result = mediator.load(LoadType.REFRESH, emptyPagingState())

        verify(remoteDataSource).getNowPlayingMovies(1)
        assertTrue(result is MediatorResult.Success)
        assertFalse((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun load_prepend_returnsEndOfPagination() = runTest {
        val result = mediator.load(LoadType.PREPEND, emptyPagingState())

        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun load_append_noRemoteKey_continuesLoading() = runTest {
        val state = pagingStateWithLastItem(movieId = 1)
        whenever(localDataSource.getMovieRemoteKeys(1)).thenReturn(null)

        val result = mediator.load(LoadType.APPEND, state)

        assertTrue(result is MediatorResult.Success)
        assertFalse((result as MediatorResult.Success).endOfPaginationReached)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun load_append_nextPageNull_stopsLoading() = runTest {
        val state = pagingStateWithLastItem(movieId = 1)
        whenever(localDataSource.getMovieRemoteKeys(1)).thenReturn(
            MovieRemoteKeyEntity(id = 1, prevPage = 3, nextPage = null, lastUpdated = 1000L)
        )

        val result = mediator.load(LoadType.APPEND, state)

        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun load_lastPage_savesToDbAndStopsLoading() = runTest {
        val response = movieResponse(page = 1, totalPages = 1)
        whenever(remoteDataSource.getNowPlayingMovies(1)).thenReturn(response)

        val result = mediator.load(LoadType.REFRESH, emptyPagingState())

        verify(localDataSource).saveNowPlayingMovies(LoadType.REFRESH, response)
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun load_apiError_returnsMediatorError() = runTest {
        whenever(remoteDataSource.getNowPlayingMovies(1)).thenThrow(RuntimeException("Network error"))

        val result = mediator.load(LoadType.REFRESH, emptyPagingState())

        assertTrue(result is MediatorResult.Error)
    }
}
