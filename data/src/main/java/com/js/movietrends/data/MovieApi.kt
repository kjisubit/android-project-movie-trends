package com.js.movietrends.data

import com.js.movietrends.domain.model.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Response<MovieList>

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Response<MovieList>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Response<MovieList>

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Response<MovieList>
}
