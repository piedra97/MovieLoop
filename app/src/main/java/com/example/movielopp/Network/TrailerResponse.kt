package com.example.movielopp.Network

import com.example.movielopp.Model.Trailer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class TrailerResponse {
    @SerializedName("results")
    @Expose
    val trailers: List<Trailer>? = null
}