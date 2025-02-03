package com.js.movietrends.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.js.movietrends.data.database.dao.MovieDao
import com.js.movietrends.data.database.dao.MovieRemoteKeyDao
import com.js.movietrends.data.database.entity.MovieEntity
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity


@Database(
    entities = [MovieEntity::class, MovieRemoteKeyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeyDao
}