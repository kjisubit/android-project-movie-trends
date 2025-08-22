package com.js.movietrends.data.paging.pagingsource


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.dto.MovieResponseDto
import com.js.movietrends.data.util.ErrorMessages

/**
 * [RemoteDataSource]에서 가져온 데이터를 페이지 단위로 로드할 수 있도록 변환
 */
class UpcomingMoviePagingSource(
    private val remoteDataSource: RemoteDataSource,
) : PagingSource<Int, MovieResponseDto>() {

    companion object {
        private const val TAG = "UpcomingMoviePagingSource"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResponseDto> {
        val page = params.key ?: 1

        return try {
            val response = remoteDataSource.getUpcomingMovies(page)
            val movieList = response.results ?: emptyList()
            LoadResult.Page(
                data = movieList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movieList.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: ErrorMessages.UNEXPECTED_ERROR, e)
            LoadResult.Error(e)
        }
    }

    /**
     * 데이터 새로고침 시 현재 앵커 포지션(anchorPosition) 기준으로
     * 가장 가까운 페이지 키를 탐색한 후, 다시 로드할 시작 페이지 결정
     */
    override fun getRefreshKey(state: PagingState<Int, MovieResponseDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}