package com.js.movietrends.data.dao

import android.content.Context
import java.io.IOException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.dao.MovieRemoteKeyDao
import com.js.movietrends.data.database.entity.MovieRemoteKeyEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieRemoteKeyDaoTest {

    private lateinit var db: MovieDb
    private lateinit var movieRemoteKeyDao: MovieRemoteKeyDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDb::class.java).build()
        movieRemoteKeyDao = db.movieRemoteKeysDao()
    }

    @After
    // Java에서 이 메서드를 호출할 때 IOException 처리를 강제하기 위한 선언 (Kotlin 단독 사용 시 불필요)
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun addAllMovieRemoteKeys_getMovieRemoteKeys_returnsInsertedKey() = runTest {
        val keys = listOf(
            MovieRemoteKeyEntity(id = 1, prevPage = null, nextPage = 2, lastUpdated = 1000L),
            MovieRemoteKeyEntity(id = 2, prevPage = 1, nextPage = 3, lastUpdated = 1000L),
        )
        movieRemoteKeyDao.addAllMovieRemoteKeys(keys)

        val result = movieRemoteKeyDao.getMovieRemoteKeys(1)
        assertEquals(1, result?.id)
        assertEquals(2, result?.nextPage)
        assertNull(result?.prevPage)
    }

    @Test
    @Throws(Exception::class)
    fun getMovieRemoteKeys_nonExistentId_returnsNull() = runTest {
        val result = movieRemoteKeyDao.getMovieRemoteKeys(999)
        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllMovieRemoteKeys_clearsTable() = runTest {
        movieRemoteKeyDao.addAllMovieRemoteKeys(listOf(
            MovieRemoteKeyEntity(id = 1, prevPage = null, nextPage = 2, lastUpdated = 1000L)
        ))
        movieRemoteKeyDao.deleteAllMovieRemoteKeys()

        val result = movieRemoteKeyDao.getMovieRemoteKeys(1)
        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun addAllMovieRemoteKeys_duplicateId_replacesExisting() = runTest {
        movieRemoteKeyDao.addAllMovieRemoteKeys(listOf(
            MovieRemoteKeyEntity(id = 1, prevPage = null, nextPage = 2, lastUpdated = 1000L)
        ))
        movieRemoteKeyDao.addAllMovieRemoteKeys(listOf(
            MovieRemoteKeyEntity(id = 1, prevPage = null, nextPage = 5, lastUpdated = 2000L)
        ))

        val result = movieRemoteKeyDao.getMovieRemoteKeys(1)
        assertEquals(5, result?.nextPage)
        assertEquals(2000L, result?.lastUpdated)
    }
}
