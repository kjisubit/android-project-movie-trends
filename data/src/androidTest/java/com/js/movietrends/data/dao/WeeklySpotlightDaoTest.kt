package com.js.movietrends.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.dao.WeeklySpotlightDao
import com.js.movietrends.data.database.entity.WeeklySpotlightEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeeklySpotlightDaoTest {

    private lateinit var db: MovieDb
    private lateinit var weeklySpotlightDao: WeeklySpotlightDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDb::class.java).build()
        weeklySpotlightDao = db.weeklySpotlightDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    private fun entity(queryKey: String, id: Int) = WeeklySpotlightEntity(
        queryKey = queryKey,
        id = id,
        overview = null,
        popularity = null,
        posterPath = null,
        title = "Movie $id",
        voteAverage = null,
        voteCount = null,
        cachedAt = 1000L
    )

    @Test
    fun getWeeklySpotlight_emptyTable_returnsNull() = runTest {
        assertNull(weeklySpotlightDao.getWeeklySpotlight())
    }

    @Test
    fun replaceWeeklySpotlight_emptyTable_insertsEntity() = runTest {
        weeklySpotlightDao.replaceWeeklySpotlight(entity("a", id = 1))

        val result = weeklySpotlightDao.getWeeklySpotlight()
        assertEquals("a", result?.queryKey)
        assertEquals(1, result?.id)
    }

    @Test
    fun replaceWeeklySpotlight_existingEntity_keepsOnlyLatest() = runTest {
        weeklySpotlightDao.replaceWeeklySpotlight(entity("a", id = 1))
        weeklySpotlightDao.replaceWeeklySpotlight(entity("b", id = 2))

        val result = weeklySpotlightDao.getWeeklySpotlight()
        assertEquals("b", result?.queryKey)
        assertEquals(2, result?.id)

        db.query("SELECT COUNT(*) FROM weekly_spotlight_entity", null).use { cursor ->
            cursor.moveToFirst()
            assertEquals(1, cursor.getInt(0))
        }
    }
}
