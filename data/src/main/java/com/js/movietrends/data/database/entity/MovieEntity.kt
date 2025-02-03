package com.js.movietrends.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_entity")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    var pk: Long = 0,
    val id: Int,
    val posterPath: String?,
    val title: String?,
)