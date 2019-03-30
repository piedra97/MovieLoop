package com.example.movielopp.Network

import com.example.movielopp.BuildConfig
import com.example.movielopp.Interfaces.OnGetMoviesCallBack
import com.example.movielopp.Interfaces.TMDbApi
import com.example.movielopp.Model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit





class MoviesRepository private constructor(private val api: TMDbApi) {

    fun getMovies(sortBy:String, callback: OnGetMoviesCallBack) {
            val call = object : Callback<MoviesResponse> {

                override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                    if (response.isSuccessful) {
                        val moviesResponse = response.body()
                        if (moviesResponse?.movies != null) {
                            callback.onSuccess((moviesResponse.movies as ArrayList<Movie>?)!!)
                        } else {
                            callback.onError()
                        }
                    } else {
                        callback.onError()
                    }
                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    callback.onError()
                }
            }

        when (sortBy) {
            TOP_RATED -> api.getTopRatedMovies(BuildConfig.TMBD_API, LANGUAGE, 1)
                .enqueue(call)
            UPCOMING -> api.getUpcomingMovies(BuildConfig.TMBD_API ,LANGUAGE, 1)
                .enqueue(call)
            POPULAR -> api.getPopularMovies(BuildConfig.TMBD_API, LANGUAGE, 1)
                .enqueue(call)
            else -> api.getPopularMovies(BuildConfig.TMBD_API,LANGUAGE, 1).enqueue(call)
        }
    }

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val LANGUAGE = "es-ES"
        val POPULAR = "popular"
        val TOP_RATED = "top_rated"
        val UPCOMING = "upcoming"

        private var repository: MoviesRepository? = null

        val instance: MoviesRepository
            get() {
                if (repository == null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    repository =
                        MoviesRepository(retrofit.create(TMDbApi::class.java))
                }

                return repository as MoviesRepository
            }
    }
}