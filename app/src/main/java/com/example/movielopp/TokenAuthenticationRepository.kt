package com.example.movielopp

import com.example.movielopp.Interfaces.OnPutAuthenticationCallBack
import com.example.movielopp.Interfaces.TMDbApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticationRepository private constructor(private val api: TMDbApi) {

    fun authenticate(requestedUser:RequestUser,callBack: OnPutAuthenticationCallBack) {
        api.authenticate(BuildConfig.TMBD_API, requestedUser).enqueue(object : Callback<TokenAuthenticationResponse> {
            override fun onFailure(call: Call<TokenAuthenticationResponse>, t: Throwable) {
                callBack.onError()
            }

            override fun onResponse(
                call: Call<TokenAuthenticationResponse>,
                response: Response<TokenAuthenticationResponse>
            ) {
                if(response.isSuccessful) {
                    val tokenAuthenticationRepository = response.body()
                    if (tokenAuthenticationRepository?.success != false ) {
                        callBack.onSuccess(tokenAuthenticationRepository?.success!!)
                    }else {
                        callBack.onError()
                    }
                }else {
                    callBack.onError()
                }
            }

        })
    }

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3/"

        private var repository: TokenAuthenticationRepository? = null

        val instance: TokenAuthenticationRepository
            get() {
                if (repository == null) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    repository =
                        TokenAuthenticationRepository(retrofit.create(TMDbApi::class.java))
                }

                return repository as TokenAuthenticationRepository
            }
    }
}