package com.example.movielopp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


class MoviesRepository private constructor(private val api: TMDbApi) {

    fun getMovies(callback: OnGetMoviesCallBack) {
        api.getPopularMovies("5c50b6853338e52e5410679185dd182f", LANGUAGE, 1)
            .enqueue(object : Callback<MoviesResponse> {

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
            })
    }

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val LANGUAGE = "es-ES"

        private var repository: MoviesRepository? = null

        val instance: MoviesRepository
            get() {
                if (repository == null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    repository = MoviesRepository(retrofit.create(TMDbApi::class.java))
                }

                return repository as MoviesRepository
            }
    }
}