package com.js.movietrends.data.migration

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.migration.ALL_MIGRATIONS
import com.js.movietrends.data.database.migration.MIGRATION_1_2
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val testDb = "migration-test"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MovieDb::class.java
    )

    @Test
    fun migrate1To2_keepsExistingMovies() {
        helper.createDatabase(testDb, 1).apply {
            execSQL(
                "INSERT INTO movie_entity (pk, id, title) VALUES (1, 100, 'Old Movie')"
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(testDb, 2, true, MIGRATION_1_2)

        db.query("SELECT title FROM movie_entity WHERE id = 100").use { cursor ->
            cursor.moveToFirst()
            assertEquals("Old Movie", cursor.getString(0))
        }
    }

    @Test
    fun migrateAll_opensLatestVersion() {
        helper.createDatabase(testDb, 1).close()

        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MovieDb::class.java,
            testDb
        ).addMigrations(*ALL_MIGRATIONS).build().apply {
            openHelper.writableDatabase.close()
        }
    }
}
