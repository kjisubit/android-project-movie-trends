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

// v2 → v3: NowPlaying 전용 테이블임을 드러내도록 movie_entity 이름 변경 (데이터 유지)
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `movie_entity` RENAME TO `now_playing_movie_entity`")
    }
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_2_3)
