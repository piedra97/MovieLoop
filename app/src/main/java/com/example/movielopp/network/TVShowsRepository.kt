package com.example.movielopp.network

import com.example.movielopp.BuildConfig
import com.example.movielopp.interfaces.*
import com.example.movielopp.model.Movie
import com.example.movielopp.model.Review
import com.example.movielopp.model.TVShow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TVShowsRepository private constructor(private val api: TMDbApi){

    fun getTVShows(sortBy:String, callback: OnGetTVShowsCallback) {
        val call = object : Callback<TVShowsResponse> {

            override fun onResponse(call: Call<TVShowsResponse>, response: Response<TVShowsResponse>) {
                if (response.isSuccessful) {
                    val tvshowsResponse = response.body()
                    if (tvshowsResponse?.tvShows != null) {
                        callback.onSuccess((tvshowsResponse.tvShows as ArrayList<TVShow>?)!!)
                    } else {
                        callback.onError()
                    }
                } else {
                    callback.onError()
                }
            }

            override fun onFailure(call: Call<TVShowsResponse>, t: Throwable) {
                callback.onError()
            }
        }

        when (sortBy) {
            TVShowsRepository.TOP_RATED -> api.getTopRatedTVShows(BuildConfig.TMBD_API, TVShowsRepository.LANGUAGE, 1)
                .enqueue(call)
            TVShowsRepository.UPCOMING -> api.getNextTVShows(BuildConfig.TMBD_API , TVShowsRepository.LANGUAGE, 1)
                .enqueue(call)
            TVShowsRepository.POPULAR -> api.getPopularTVShows(BuildConfig.TMBD_API, TVShowsRepository.LANGUAGE, 1)
                .enqueue(call)
            else -> api.getPopularTVShows(BuildConfig.TMBD_API, TVShowsRepository.LANGUAGE, 1).enqueue(call)
        }
    }

    fun getTVShow(tvShowId: Int, callback: OnGetTVShowCallback) {
        api.getTVShow(tvShowId, BuildConfig.TMBD_API, TVShowsRepository.LANGUAGE).
            enqueue(object : Callback<TVShow> {
                override fun onFailure(call: Call<TVShow>, t: Throwable) {
                    callback.onError()
                }

                override fun onResponse(call: Call<TVShow>, response: Response<TVShow>) {
                    if(response.isSuccessful) {
                        val tvshow = response.body()
                        if (tvshow != null) {
                            callback.onSuccess(tvshow)
                        }else {
                            callback.onError()
                        }
                    }else {
                        callback.onError()
                    }
                }

            })
    }

    fun getVideos(tvShowId: Int, callback: OnGetTrailersCallback) {
        api.getVideos(tvShowId, BuildConfig.TMBD_API, "en-US").enqueue(object: Callback<TrailerResponse> {
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

    fun getReviews(tvshowID:Int, callback: OnGetReviewsCallback) {
        api.getTVReviews(tvshowID, BuildConfig.TMBD_API, "en-US").enqueue(object: Callback<ReviewResponse> {
            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                callback.onError()
            }

            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if(response.isSuccessful) {
                    val reviewResponse = response.body()
                    if (reviewResponse?.reviews != null) {
                        callback.onSuccess(reviewResponse.reviews as ArrayList<Review>)
                    }else {
                        callback.onError()
                    }
                }else {
                    callback.onError()
                }
            }

        })
    }

    fun getCredits(tvID: Int, callback: OnGetTVShowCreditsCallback) {
        api.getTVShowCredits(tvID, BuildConfig.TMBD_API).enqueue(object: Callback<CreditResponse> {
            override fun onFailure(call: Call<CreditResponse>, t: Throwable) {
                callback.onError()
            }

            override fun onResponse(call: Call<CreditResponse>, response: Response<CreditResponse>) {
                if (response.isSuccessful) {
                    val creditResponse = response.body()
                    if (creditResponse?.cast != null) {
                        callback.onSuccess(creditResponse.cast!!)
                    } else {
                        callback.onError()
                    }
                } else {
                    callback.onError()
                }
            }

        })
    }

    fun getSearchedTVShow(query:String, callback:OnGetSearchedTV) {
        api.searchTVShow(BuildConfig.TMBD_API, TVShowsRepository.LANGUAGE, query, 1).enqueue(object : Callback<TVShowsResponse> {
            override fun onFailure(call: Call<TVShowsResponse>, t: Throwable) {
                callback.onError()
            }

            override fun onResponse(call: Call<TVShowsResponse>, response: Response<TVShowsResponse>) {
                if (response.isSuccessful) {
                    val searchedTV = response.body()
                    if (searchedTV?.tvShows != null) {
                        callback.onSuccess(searchedTV.tvShows as ArrayList<TVShow>)
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
        const val UPCOMING = "airing_today"

        private var repository: TVShowsRepository? = null

        val instance: TVShowsRepository
            get() {
                if (repository == null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    repository =
                        TVShowsRepository(retrofit.create(TMDbApi::class.java))
                }

                return repository as TVShowsRepository
            }
    }
}