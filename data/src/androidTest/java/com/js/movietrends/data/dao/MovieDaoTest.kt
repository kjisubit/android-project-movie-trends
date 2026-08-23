package com.js.movietrends.data.dao

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.js.movietrends.data.database.MovieDb
import com.js.movietrends.data.database.dao.MovieDao
import com.js.movietrends.data.database.entity.MovieEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    private lateinit var db: MovieDb
    private lateinit var movieDao: MovieDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MovieDb::class.java).build()
        movieDao = db.movieDao()
    }

    @After
    // Java에서 이 메서드를 호출할 때 IOException 처리를 강제하기 위한 선언 (Kotlin 단독 사용 시 불필요)
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun addMovies_getAllMovies_returnsInsertedData() = runTest {
        val movies = listOf(
            MovieEntity(
                id = 1,
                title = "Movie A",
                overview = null,
                popularity = null,
                posterPath = null,
                voteAverage = null,
                voteCount = null
            ),
            MovieEntity(
                id = 2,
                title = "Movie B",
                overview = null,
                popularity = null,
                posterPath = null,
                voteAverage = null,
                voteCount = null
            ),
        )
        movieDao.addMovies(movies)

        val result = movieDao.getAllMovies().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )

        val page = result as PagingSource.LoadResult.Page
        assertEquals(2, page.data.size)
        assertEquals(1, page.data[0].id)
        assertEquals(2, page.data[1].id)
    }

    @Test
    @Throws(Exception::class)
    fun addMovies_duplicatePk_replacesExisting() = runTest {
        // pk를 명시해야 REPLACE 전략이 동작 (pk = 0이면 auto-generate로 신규 row 생성)
        movieDao.addMovies(
            listOf(
                MovieEntity(
                    pk = 1,
                    id = 1,
                    title = "Old Title",
                    overview = null,
                    popularity = null,
                    posterPath = null,
                    voteAverage = null,
                    voteCount = null
                )
            )
        )
        movieDao.addMovies(
            listOf(
                MovieEntity(
                    pk = 1,
                    id = 1,
                    title = "New Title",
                    overview = null,
                    popularity = null,
                    posterPath = null,
                    voteAverage = null,
                    voteCount = null
                )
            )
        )

        val result = movieDao.getMovie(1).first()
        assertEquals("New Title", result.title)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllMovies_clearsTable() = runTest {
        movieDao.addMovies(
            listOf(
                MovieEntity(
                    id = 1,
                    title = "Movie A",
                    overview = null,
                    popularity = null,
                    posterPath = null,
                    voteAverage = null,
                    voteCount = null
                )
            )
        )
        movieDao.deleteAllMovies()

        val result = movieDao.getAllMovies().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false)
        )

        val page = result as PagingSource.LoadResult.Page
        assertEquals(0, page.data.size)
    }

    @Test
    @Throws(Exception::class)
    fun getMovie_returnsCorrectMovieById() = runTest {
        movieDao.addMovies(
            listOf(
                MovieEntity(
                    id = 1,
                    title = "Movie A",
                    overview = null,
                    popularity = null,
                    posterPath = null,
                    voteAverage = null,
                    voteCount = null
                ),
                MovieEntity(
                    id = 2,
                    title = "Movie B",
                    overview = null,
                    popularity = null,
                    posterPath = null,
                    voteAverage = null,
                    voteCount = null
                ),
            )
        )

        val result = movieDao.getMovie(2).first()
        assertEquals(2, result.id)
        assertEquals("Movie B", result.title)
    }
}
