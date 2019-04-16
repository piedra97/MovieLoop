package com.example.movielopp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class RequestTokenResponse {

    @SerializedName("request_token")
    @Expose
    var requestToken:String? = null

}