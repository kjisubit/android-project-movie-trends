package com.js.movietrends.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction

import com.js.movietrends.data.BuildConfig
import com.js.movietrends.data.api.MovieApi
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.domain.core.AppInfoManager
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NowPlayingRemoteMediator(private val movieApi: MovieApi, private val movieDb: MovieDb) :
    RemoteMediator<Int, MovieEntity>() {

    private val movieDao = movieDb.movieDao()
    private val movieRemoteKeysDao = movieDb.movieRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            // api 호출부에 넘길 page 결정
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeyEntity = getRemoteKeyEntityOfLastItem(state)
                    remoteKeyEntity?.nextPage?.minus(1) ?: 1
                }
            }

            val response = movieApi.getNowPlayingMovies(
                apiKey = BuildConfig.TMDB_API_KEY,
                language = AppInfoManager.localeCode,
                page = page
            )

            var endOfPaginationReached = false
            if (response.isSuccessful) {
                val responseData = response.body()
                endOfPaginationReached = responseData == null
                responseData?.let {
                    // db 내부 transaction 수행
                    movieDb.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            movieDao.deleteAllMovies()
                            movieRemoteKeysDao.deleteAllMovieRemoteKeys()
                        }
                    }

                    val currentPage = responseData.page
                    val nextPage: Int = currentPage + 1
                    val prevPage: Int? = if (currentPage <= 1) null else currentPage - 1

                    // RemoteKeyEntity 목록 저장
                    responseData.results?.map { movieResponse ->
                        MovieRemoteKeyEntity(
                            id = movieResponse.id,
                            prevPage = prevPage,
                            nextPage = nextPage,
                            lastUpdated = System.currentTimeMillis(),
                        )
                    }?.let { movieRemoteKeysDao.addAllMovieRemoteKeys(movieRemoteKeys = it) }

                    // MovieEntity 목록 저장
                    responseData.results?.map { movieResponse ->
                        MovieEntity(
                            id = movieResponse.id,
                            posterPath = movieResponse.posterPath,
                            title = movieResponse.title
                        )
                    }?.let { movieDao.addMovies(movies = it) }
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyEntityOfLastItem(
        state: PagingState<Int, MovieEntity>,
    ): MovieRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                movieRemoteKeysDao.getMovieRemoteKeys(movieId = movie.id)
            }
    }
}