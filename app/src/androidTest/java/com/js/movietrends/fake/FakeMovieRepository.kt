package com.js.movietrends.fake

import androidx.paging.PagingData
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
        posterPath = null,
        voteAverage = 8.0,
        voteCount = 100
    )

    override fun getDiscoveredMovies(
        startDate: String,
        endDate: String,
        sortBy: String
    ): Flow<ApiResult<Movie>> = flowOf(ApiResult.Success(movie))

    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> =
        flowOf(PagingData.from(listOf(movie)))

    override fun getUpcomingMovies(): Flow<PagingData<Movie>> =
        flowOf(PagingData.from(listOf(movie)))
}
