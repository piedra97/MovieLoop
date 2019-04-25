package com.example.movielopp.interfaces


import com.example.movielopp.model.Movie
import com.example.movielopp.model.TVShow
import com.example.movielopp.network.*
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET


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

    @GET("movie/{movie_id}/reviews")
    fun getReviews(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): Call<ReviewResponse>

    @GET("movie/{movie_id}/credits")
    fun getCredits(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKEy: String
    ): Call<CreditResponse>

    @GET("tv/popular")
    fun getPopularTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TVShowsResponse>

    @GET("tv/top_rated")
    fun getTopRatedTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TVShowsResponse>

    @GET("tv/airing_today")
    fun getNextTVShows(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<TVShowsResponse>

    @GET("tv/{tv_id}")
    fun getTVShow(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): Call<TVShow>


    @GET("tv/{tv_id}/videos")
    fun getVideos(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): Call<TrailerResponse>


    @GET("tv/{tv_id}/reviews")
    fun getTVReviews(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKEy: String,
        @Query("language") language: String
    ): Call<ReviewResponse>


    @GET("tv/{tv_id}/credits")
    fun getTVShowCredits(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKEy: String
    ): Call<CreditResponse>






}