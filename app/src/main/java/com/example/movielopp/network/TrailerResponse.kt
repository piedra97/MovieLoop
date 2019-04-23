package com.example.movielopp.network

import com.example.movielopp.model.Trailer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class TrailerResponse {
    @SerializedName("results")
    @Expose
    val trailers: List<Trailer>? = null
}