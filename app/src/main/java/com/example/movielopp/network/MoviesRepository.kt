package com.example.movielopp.network

import com.example.movielopp.BuildConfig
import com.example.movielopp.interfaces.*
import com.example.movielopp.model.Movie
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

    fun getMovie(movieId: Int, callback:OnGetMovieCallBack) {
        api.getMovie(movieId, BuildConfig.TMBD_API, LANGUAGE).
            enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    callback.onError()
                }

                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    if(response.isSuccessful) {
                        val movie = response.body()
                        if (movie != null) {
                            callback.onSuccess(movie)
                        }else {
                            callback.onError()
                        }
                    }else {
                        callback.onError()
                    }
                }

            })
    }

    fun getGenres(callback: OnGetGenresCallback){
        api.getGenres(BuildConfig.TMBD_API, LANGUAGE).
            enqueue(object: Callback<GenresResponse> {
                override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                    callback.onError()
                }

                override fun onResponse(call: Call<GenresResponse>, response: Response<GenresResponse>) {

                    if (response.isSuccessful) {
                        val genresResponse = response.body()
                        if (genresResponse != null) {
                            callback.onSuccess(genresResponse.genres!!)
                        }else {
                            callback.onError()
                        }
                    }else {
                        callback.onError()
                    }
                }

            })
    }

    fun getTrailers(movieId: Int, callback: OnGetTrailersCallback) {
        api.getTrailers(movieId, BuildConfig.TMBD_API, "en-US").enqueue(object: Callback<TrailerResponse> {
            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                callback.onError()
            }

            override fun onResponse(call: Call<TrailerResponse>, response: Response<TrailerResponse>) {
                if(response.isSuccessful) {
                    val trailerResponse = response.body()
                    if (trailerResponse?.trailers != null) {
                        callback.onSuccess(trailerResponse.trailers)
                    } else {
                        callback.onError()
                    }
                }else {
                    callback.onError()
                }
            }

        })
    }

    fun getReviews(movieID:Int, callback: OnGetReviewsCallback) {
        api.getReviews(movieID, BuildConfig.TMBD_API, "en-US").enqueue(object: Callback<ReviewResponse> {
            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                callback.onError()
            }

            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if(response.isSuccessful) {
                    val reviewResponse = response.body()
                    if (reviewResponse?.reviews != null) {
                        callback.onSuccess(reviewResponse.reviews)
                    }else {
                        callback.onError()
                    }
                }else {
                    callback.onError()
                }
            }

        })
    }

    fun getCredits(movieID: Int, callback: OnGetCreditsCallback) {
        api.getCredits(movieID, BuildConfig.TMBD_API).enqueue(object: Callback<CreditResponse> {
            override fun onFailure(call: Call<CreditResponse>, t: Throwable) {
                callback.onError()
            }

            override fun onResponse(call: Call<CreditResponse>, response: Response<CreditResponse>) {
                if (response.isSuccessful) {
                    val creditResponse = response.body()
                    if (creditResponse?.cast != null && creditResponse.crew != null) {
                        callback.onSuccess(creditResponse.cast!!, creditResponse.crew!!)
                    } else {
                        callback.onError()
                    }
                } else {
                    callback.onError()
                }
            }

        })
    }

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val LANGUAGE = "es-ES"
        const val POPULAR = "popular"
        const val TOP_RATED = "top_rated"
        const val UPCOMING = "upcoming"

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