package com.js.movietrends.data.datasource

import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.dto.MovieListResponseDto

class LocalDataSourceImpl(private val movieDb: MovieDb) : LocalDataSource {

    private val movieDao = movieDb.movieDao()
    private val movieRemoteKeysDao = movieDb.movieRemoteKeysDao()

    override fun getAllMovies(): PagingSource<Int, MovieEntity> {
        return movieDao.getAllMovies()
    }

    override suspend fun getMovieRemoteKeys(movieId: Int): MovieRemoteKeyEntity? {
        return movieRemoteKeysDao.getMovieRemoteKeys(movieId)
    }

    override suspend fun deleteAllMovies() {
        movieDao.deleteAllMovies()
    }

    override suspend fun deleteAllMovieRemoteKeys() {
        movieRemoteKeysDao.deleteAllMovieRemoteKeys()
    }

    override suspend fun addMovies(movies: List<MovieEntity>) {
        movieDao.addMovies(movies)
    }

    override suspend fun addRemoteKeys(keys: List<MovieRemoteKeyEntity>) {
        movieRemoteKeysDao.addAllMovieRemoteKeys(keys)
    }

    override suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): MovieRemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
            movieRemoteKeysDao.getMovieRemoteKeys(movie.id)
        }
    }

    override suspend fun saveNowPlayingMovies(loadType: LoadType, response: MovieListResponseDto) {
        val currentPage = response.page
        val nextPage = currentPage + 1
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
            MovieEntity(
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
                deleteAllMovies()
                deleteAllMovieRemoteKeys()
            }
            remoteKeys?.let { addRemoteKeys(it) }
            movieEntities?.let { addMovies(it) }
        }
    }
}