package com.js.movietrends.fake

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMovieRepository : MovieRepository {

    companion object {
        const val MOVIE_ID = 1
        const val MOVIE_TITLE = "Fake Movie"
    }

    private val movie = Movie(
        id = MOVIE_ID,
        title = MOVIE_TITLE,
        overview = "Fake overview",
        popularity = 9.0,
        posterPath = "/fake_poster.jpg", // FakeImageLoaderEngine이 모든 URL을 가로채므로 경로 값 자체는 의미 없음
        voteAverage = 8.0,
        voteCount = 100
    )

    override fun getDiscoveredMovies(
        startDate: String,
        endDate: String,
        sortBy: String
    ): Flow<ApiResult<Movie>> = flowOf(ApiResult.Success(movie))

    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> = fakePager()

    override fun getUpcomingMovies(): Flow<PagingData<Movie>> = fakePager()

    private fun fakePager() = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            object : PagingSource<Int, Movie>() {
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
                    LoadResult.Page(data = listOf(movie), prevKey = null, nextKey = null)

                override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null
            }
        }
    ).flow
}
