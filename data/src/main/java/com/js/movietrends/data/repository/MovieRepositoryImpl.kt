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
import com.js.movietrends.data.paging.pagingsource.UpcomingMoviePagingSource
import com.js.movietrends.data.paging.remotemediator.NowPlayingMovieMediator
import com.js.movietrends.data.util.ErrorMessages
import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.Movie
import com.js.movietrends.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : MovieRepository {

    companion object {
        private const val TAG = "MovieRepositoryImpl"
    }

    // 페이징 없음, 캐싱 있음 - Room을 SSOT로 사용
    override fun getDiscoveredMovies(
        startDate: String,
        endDate: String,
        sortBy: String
    ): Flow<ApiResult<Movie>> = flow {
        val queryKey = "$startDate|$endDate|$sortBy"
        val cached = localDataSource.getWeeklySpotlight()

        if (cached?.queryKey == queryKey) {
            emit(ApiResult.Success(ModelMapper.mapWeeklySpotlightEntityToDomain(cached)))
            return@flow
        }

        try {
            val dto = remoteDataSource.getDiscoveredMovies(
                startDate = startDate,
                endDate = endDate,
                sortBy = sortBy
            )
            val movie = dto.results?.firstOrNull()
                ?: throw NoSuchElementException(ErrorMessages.NO_DISCOVERED_MOVIE)
            localDataSource.replaceWeeklySpotlight(
                ModelMapper.mapMovieResponseDtoToWeeklySpotlightEntity(
                    movieResponseDto = movie,
                    queryKey = queryKey,
                    cachedAt = System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "", e)
            if (cached == null) {
                emit(ApiResult.Error(e))
                return@flow
            }
        }

        // 저장에 성공했으면 새 캐시를, 실패했으면 이전 캐시 emit
        val entity = localDataSource.getWeeklySpotlight()
        if (entity != null) {
            emit(ApiResult.Success(ModelMapper.mapWeeklySpotlightEntityToDomain(entity)))
        } else {
            emit(ApiResult.Error(IllegalStateException(ErrorMessages.UNEXPECTED_ERROR)))
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