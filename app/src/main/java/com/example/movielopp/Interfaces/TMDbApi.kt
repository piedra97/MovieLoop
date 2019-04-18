package com.example.movielopp.Interfaces


import com.example.movielopp.Model.Movie
import com.example.movielopp.Network.MoviesResponse
import com.example.movielopp.RequestTokenResponse
import com.example.movielopp.RequestUser
import com.example.movielopp.TokenAuthenticationResponse
import retrofit2.Call
import retrofit2.http.*
import com.example.movielopp.Network.GenresResponse
import retrofit2.http.GET
import com.example.movielopp.Network.TrailerResponse






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

    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): Call<Movie>

    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Call<GenresResponse>

    @GET("movie/{movie_id}/videos")
    fun getTrailers(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): Call<TrailerResponse>


}