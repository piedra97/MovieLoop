package com.example.movielopp.Network

import com.example.movielopp.BuildConfig
import com.example.movielopp.Interfaces.OnGetTVShowsCallback
import com.example.movielopp.Interfaces.TMDbApi
import com.example.movielopp.Model.TVShow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TVShowsRepository private constructor(private val api: TMDbApi){

    fun getTVShow(callback: OnGetTVShowsCallback) {
        api.getPopularTVShows(BuildConfig.TMBD_API, LANGUAGE, 1).
            enqueue(object : Callback<TVShowsResponse> {
            override fun onFailure(call: Call<TVShowsResponse>, t: Throwable) {
                callback.onError()
            }

            override fun onResponse(call: Call<TVShowsResponse>, response: Response<TVShowsResponse>) {
                if(response.isSuccessful) {
                    val tvshowResponse = response.body()
                    if (tvshowResponse?.tvShows != null) {
                        callback.onSuccess(tvshowResponse.tvShows as ArrayList<TVShow>)
                    }else {
                        callback.onError()
                    }
                }else {
                    callback.onError()
                }
            }

        })

    }

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val LANGUAGE = "es-ES"
        val POPULAR = "popular"
        val TOP_RATED = "top_rated"
        val UPCOMING = "upcoming"

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