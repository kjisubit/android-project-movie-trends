package com.js.movietrends.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.js.movietrends.data.datasource.LocalDataSource
import com.js.movietrends.data.datasource.RemoteDataSource
import com.js.movietrends.data.mapper.ModelMapper
import com.js.movietrends.data.mapper.toErrorType
import com.js.movietrends.data.paging.pagingsource.UpcomingMoviePagingSource
import com.js.movietrends.data.paging.remotemediator.NowPlayingMovieMediator
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.repository.MovieRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : MovieRepository {

    companion object {
        private const val TAG = "MovieRepositoryImpl"
    }

    // 페이징 없음, 캐싱 있음 - queryKey가 같으면 캐시 사용, 다르면 API 호출 후 캐시 교체
    override suspend fun getWeeklySpotlight(
        startDate: String,
        endDate: String,
        sortBy: String
    ): ApiResult<Movie> {
        val queryKey = "$startDate|$endDate|$sortBy"
        return try {
            val cached = localDataSource.getWeeklySpotlight()
            if (cached?.queryKey == queryKey) {
                return ApiResult.Success(ModelMapper.mapWeeklySpotlightEntityToDomain(cached))
            }

            // API 실패 시, 캐시가 있으면 반환하고 없으면 catch에서 Error 처리
            val movie = try {
                remoteDataSource.getDiscoveredMovies(
                    startDate = startDate,
                    endDate = endDate,
                    sortBy = sortBy
                ).results?.firstOrNull()
                    ?: throw NoSuchElementException("No movie found for $queryKey")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (cached == null) throw e
                Log.e(TAG, "Fetch failed, using stale cache", e)
                return ApiResult.Success(ModelMapper.mapWeeklySpotlightEntityToDomain(cached))
            }

            // DB 저장에 실패하더라도 api에서 받아 온 영화는 리턴
            val entity = ModelMapper.mapMovieResponseDtoToWeeklySpotlightEntity(
                movieResponseDto = movie,
                queryKey = queryKey,
                cachedAt = System.currentTimeMillis()
            )
            try {
                localDataSource.replaceWeeklySpotlight(entity)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to cache weekly spotlight", e)
            }
            ApiResult.Success(ModelMapper.mapWeeklySpotlightEntityToDomain(entity))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load weekly spotlight", e)
            ApiResult.Error(e.toErrorType())
        }
    }

    // 페이징 있음, 캐싱 있음 — RemoteMediator가 API 결과를 Room에 저장하고 로컬 DB에서 페이징
    @OptIn(ExperimentalPagingApi::class)
    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        // 온라인/오프라인 환경에 상관 없이 DB에 캐싱된 데이터를 가져오도록 팩토리 선언
        val pagingSourceFactory = { localDataSource.getAllMovies() }

        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = NowPlayingMovieMediator(remoteDataSource, localDataSource),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { entity -> ModelMapper.mapMovieEntityToDomain(entity) }
        }
    }

    // 페이징 있음, 캐싱 없음 — 매번 API에서 직접 페이징
    override fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                UpcomingMoviePagingSource(remoteDataSource)
            }
        ).flow.map { pagingData ->
            pagingData.map { dto -> ModelMapper.mapMovieResponseDtoToDomain(dto) }
        }
    }
}