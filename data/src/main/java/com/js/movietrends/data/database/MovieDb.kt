package com.js.movietrends.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.js.movietrends.data.database.dao.MovieDao
import com.js.movietrends.data.database.dao.MovieRemoteKeyDao
import com.js.movietrends.data.database.dao.WeeklySpotlightDao
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import com.js.movietrends.data.database.entity.WeeklySpotlightEntity


@Database(
    entities = [MovieEntity::class, MovieRemoteKeyEntity::class, WeeklySpotlightEntity::class],
    version = 2,
    exportSchema = true
)
abstract class MovieDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeyDao
    abstract fun weeklySpotlightDao(): WeeklySpotlightDao
}
