package com.js.movietrends.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.js.movietrends.data.database.entity.WeeklySpotlightEntity

@Dao
interface WeeklySpotlightDao {
    // 테이블에는 항상 최대 1건만 존재
    @Query("SELECT * FROM weekly_spotlight_entity LIMIT 1")
    suspend fun getWeeklySpotlight(): WeeklySpotlightEntity?

    @Insert
    suspend fun insertWeeklySpotlight(entity: WeeklySpotlightEntity)

    @Query("DELETE FROM weekly_spotlight_entity")
    suspend fun deleteAllWeeklySpotlights()

    // 이전 캐시를 지우고 새 캐시 1건으로 교체 (원자적으로 실행)
    @Transaction
    suspend fun replaceWeeklySpotlight(entity: WeeklySpotlightEntity) {
        deleteAllWeeklySpotlights()
        insertWeeklySpotlight(entity)
    }
}
