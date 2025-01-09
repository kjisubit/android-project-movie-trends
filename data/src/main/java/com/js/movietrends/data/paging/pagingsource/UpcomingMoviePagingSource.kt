package com.js.movietrends.data.paging.pagingsource


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.js.movietrends.data.BuildConfig
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.domain.core.AppInfoManager
import com.js.movietrends.domain.model.Movie
import retrofit2.HttpException

class UpcomingMoviePagingSource(private val movieApi: MovieApi) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        try {
            val response =
                movieApi.getUpcomingMovies(
                    apiKey = BuildConfig.TMDB_API_KEY,
                    language = AppInfoManager.localeCode,
                    page = page
                )
            if (response.isSuccessful) {
                val movieList = response.body()?.results?.filterNotNull() ?: emptyList()
                return LoadResult.Page(
                    data = movieList,
                    prevKey = null, // Only paging forward.
                    //prevKey = if (page == 1) null else page - 1,
                    nextKey = if (movieList.isEmpty()) null else page + 1
                )
            } else {
                return LoadResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}