package com.example.movielopp

import com.example.movielopp.Interfaces.OnGetRequestTokenCallback
import com.example.movielopp.Interfaces.TMDbApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestTokenRepository private constructor(private val api: TMDbApi) {

    fun getRequestToken(callback: OnGetRequestTokenCallback) {
        api.getRequestToken(BuildConfig.TMBD_API).enqueue(object : Callback<RequestTokenResponse> {
            override fun onFailure(call: Call<RequestTokenResponse>, t: Throwable) {
                callback.onError()
            }

            override fun onResponse(call: Call<RequestTokenResponse>, response: Response<RequestTokenResponse>) {
                if(response.isSuccessful) {
                    val requestTokenResponse = response.body()
                    if (requestTokenResponse?.requestToken != null ) {
                        callback.onSuccess(requestTokenResponse.requestToken!!)
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

        private var repository: RequestTokenRepository? = null

        val instance: RequestTokenRepository
            get() {
                if (repository == null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    repository =
                        RequestTokenRepository(retrofit.create(TMDbApi::class.java))
                }

                return repository as RequestTokenRepository
            }
    }
}

