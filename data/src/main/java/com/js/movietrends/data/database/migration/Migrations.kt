package com.js.movietrends.data.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// v1 → v2: Weekly Spotlight 캐시 테이블 추가 (기존 테이블은 그대로 유지)
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `weekly_spotlight_entity` (
                `queryKey` TEXT NOT NULL,
                `id` INTEGER NOT NULL,
                `overview` TEXT,
                `popularity` REAL,
                `posterPath` TEXT,
                `title` TEXT,
                `voteAverage` REAL,
                `voteCount` INTEGER,
                `cachedAt` INTEGER NOT NULL,
                PRIMARY KEY(`queryKey`)
            )
            """.trimIndent()
        )
    }
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2)
