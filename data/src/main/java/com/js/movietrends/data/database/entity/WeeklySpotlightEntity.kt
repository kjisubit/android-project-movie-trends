package com.js.movietrends.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Weekly Spotlight 캐시. 항상 최신 1건만 유지한다.
 *
 * @property queryKey 조회 조건(기간 + 정렬)으로 만든 키. 요청 조건과 같으면 캐시를 그대로 사용한다.
 * @property cachedAt 캐시 저장 시각(ms)
 */
@Entity(tableName = "weekly_spotlight_entity")
data class WeeklySpotlightEntity(
    @PrimaryKey
    val queryKey: String,
    val id: Int,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val cachedAt: Long,
)
