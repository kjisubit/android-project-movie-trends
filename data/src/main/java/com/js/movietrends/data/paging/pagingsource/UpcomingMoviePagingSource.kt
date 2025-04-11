package com.js.movietrends.data.paging.pagingsource


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.dto.MovieResponseDto
import com.js.movietrends.domain.model.ApiResult

class UpcomingMoviePagingSource(
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, MovieResponseDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponseDto> {
        val page = params.key ?: 1
        return try {
            when (val result = remoteDataSource.getUpcomingMovies(page)) {
                is ApiResult.Success -> {
                    val movieList = result.data.results ?: emptyList()
                    LoadResult.Page(
                        data = movieList,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (movieList.isEmpty()) null else page + 1
                    )
                }

                is ApiResult.Error -> {
                    LoadResult.Error(result.exception)
                }

                is ApiResult.Loading -> {
                    LoadResult.Error(Exception("Unexpected loading state"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponseDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}