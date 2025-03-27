package com.js.movietrends

import com.js.movietrends.domain.model.ApiResult
import com.js.movietrends.domain.model.SampleData
import com.js.movietrends.domain.repository.MovieRepository
import com.js.movietrends.domain.usecase.GetWeeklySpotlightedMovieUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class GetPopularMoviesUseCaseTest {

    @Mock
    private lateinit var movieRepository: MovieRepository

    private lateinit var getWeeklySpotlightedMovieUseCase: GetWeeklySpotlightedMovieUseCase

    @Before
    fun setup() {
        getWeeklySpotlightedMovieUseCase = GetWeeklySpotlightedMovieUseCase(movieRepository)
    }

    @Test
    fun `invoke should return weekly spotlighted movie`() = runTest {

        // Given
        val fakeMovie = SampleData.createDummyMovie()
        whenever(movieRepository.getWeeklySpotlightedMovie()).thenReturn(
            flowOf(
                ApiResult.Success(
                    fakeMovie
                )
            )
        )

        // When
        val result = getWeeklySpotlightedMovieUseCase.invoke().first()

        // Then
        assertEquals(ApiResult.Success(fakeMovie), result)
    }
}