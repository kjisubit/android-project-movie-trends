package com.js.movietrends.data.datasource

import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.entity.NowPlayingMovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.database.entity.WeeklySpotlightEntity
import com.js.movietrends.data.dto.MovieListResponseDto

class LocalDataSourceImpl(private val movieDb: MovieDb) : LocalDataSource {

    private val nowPlayingMovieDao = movieDb.nowPlayingMovieDao()
    private val movieRemoteKeysDao = movieDb.movieRemoteKeysDao()
    private val weeklySpotlightDao = movieDb.weeklySpotlightDao()

    override suspend fun getWeeklySpotlight(): WeeklySpotlightEntity? {
        return weeklySpotlightDao.getWeeklySpotlight()
    }

    override suspend fun replaceWeeklySpotlight(entity: WeeklySpotlightEntity) {
        weeklySpotlightDao.replaceWeeklySpotlight(entity)
    }

    override fun getNowPlayingMovies(): PagingSource<Int, NowPlayingMovieEntity> {
        return nowPlayingMovieDao.getNowPlayingMovies()
    }

    override suspend fun getMovieRemoteKeys(movieId: Int): MovieRemoteKeyEntity? {
        return movieRemoteKeysDao.getMovieRemoteKeys(movieId)
    }

    override suspend fun deleteAllNowPlayingMovies() {
        nowPlayingMovieDao.deleteAllNowPlayingMovies()
    }

    override suspend fun deleteAllMovieRemoteKeys() {
        movieRemoteKeysDao.deleteAllMovieRemoteKeys()
    }

    override suspend fun addNowPlayingMovies(movies: List<NowPlayingMovieEntity>) {
        nowPlayingMovieDao.addNowPlayingMovies(movies)
    }

    override suspend fun addRemoteKeys(keys: List<MovieRemoteKeyEntity>) {
        movieRemoteKeysDao.addAllMovieRemoteKeys(keys)
    }

    override suspend fun saveNowPlayingMovies(loadType: LoadType, response: MovieListResponseDto) {
        val currentPage = response.page
        val nextPage = if (currentPage >= (response.totalPages ?: 1)) null else currentPage + 1
        val prevPage: Int? = if (currentPage <= 1) null else currentPage - 1

        val remoteKeys = response.results?.map {
            MovieRemoteKeyEntity(
                id = it.id,
                prevPage = prevPage,
                nextPage = nextPage,
                lastUpdated = System.currentTimeMillis(),
            )
        }

        val movieEntities = response.results?.map {
            NowPlayingMovieEntity(
                id = it.id,
                posterPath = it.posterPath,
                title = it.title,
                overview = it.overview,
                popularity = it.popularity,
                voteAverage = it.voteAverage,
                voteCount = it.voteCount,
            )
        }

        movieDb.withTransaction {
            if (loadType == LoadType.REFRESH) {
                deleteAllNowPlayingMovies()
                deleteAllMovieRemoteKeys()
            }
            remoteKeys?.let { addRemoteKeys(it) }
            movieEntities?.let { addNowPlayingMovies(it) }
        }
    }
}
