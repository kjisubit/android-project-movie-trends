package com.js.movietrends.data.api

import com.js.movietrends.data.model.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Response<MovieListResponse>

    @GET("discover/movie")
    suspend fun getDiscoveredMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int = 1,
        @Query("primary_release_date.gte") primaryReleaseDateGte: String,
        @Query("primary_release_date.lte") primaryReleaseDateLte: String,
    ): Response<MovieListResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Response<MovieListResponse>
}
