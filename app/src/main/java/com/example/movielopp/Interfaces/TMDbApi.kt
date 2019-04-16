package com.example.movielopp.Interfaces


import com.example.movielopp.Network.MoviesResponse
import com.example.movielopp.RequestTokenResponse
import com.example.movielopp.RequestUser
import com.example.movielopp.TokenAuthenticationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface TMDbApi {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MoviesResponse>

    @GET("authentication/token/new")
    fun getRequestToken(
        @Query("api_key") apiKey: String
    ): Call<RequestTokenResponse>

    @POST("authentication/session/new")
    fun authenticate(
        @Query("api_key") apiKey: String,
        @Body requestedUser: RequestUser
    ): Call<TokenAuthenticationResponse>

}