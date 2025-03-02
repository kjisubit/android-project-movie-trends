package com.js.movietrends.domain.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlin.random.Random

object SampleData {
    /**
     * 더미 Movie 데이터 생성
     */
    fun createDummyMovie(
        id: Int = 0,
        overview: String = "This is a sample overview",
        popularity: Double = 1.0,
        posterPath: String = "",
        title: String = "Sample Movie",
        voteAverage: Double = 1.0,
        voteCount: Int = 1
    ): Movie {
        return Movie(
            id = id,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            title = title,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }

    /**
     * 더미 페이징 소스 생성
     */
    fun createDummyMoviePagingSource(isSuccess: Boolean = true): PagingSource<Int, Movie> {
        return object : PagingSource<Int, Movie>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
                return try {
                    if (isSuccess) {
                        val size = 20
                        val movies = List(size) {
                            createDummyMovie(id = Random.nextInt(0, size))
                        }
                        LoadResult.Page(
                            data = movies,
                            prevKey = null,
                            nextKey = null
                        )
                    } else {
                        LoadResult.Error(Throwable("Paging Error"))
                    }
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

            override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
                return state.anchorPosition?.let { position ->
                    val item = state.closestItemToPosition(position)
                    item?.id
                }
            }
        }
    }
}