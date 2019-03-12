package com.example.movielopp


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface TMDbApi {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesResponse>
}