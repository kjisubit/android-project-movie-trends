package com.js.movietrends.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.js.movietrends.data.database.dao.NowPlayingMovieDao
import com.js.movietrends.data.database.dao.MovieRemoteKeyDao
import com.js.movietrends.data.database.dao.WeeklySpotlightDao
import com.js.movietrends.data.database.entity.NowPlayingMovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.database.entity.WeeklySpotlightEntity


@Database(
    entities = [NowPlayingMovieEntity::class, MovieRemoteKeyEntity::class, WeeklySpotlightEntity::class],
    version = 3,
    exportSchema = true
)
abstract class MovieDb : RoomDatabase() {
    abstract fun nowPlayingMovieDao(): NowPlayingMovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeyDao
    abstract fun weeklySpotlightDao(): WeeklySpotlightDao
}
