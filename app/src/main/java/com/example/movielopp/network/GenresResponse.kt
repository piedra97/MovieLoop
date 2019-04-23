package com.example.movielopp.network

import com.example.movielopp.model.Genre
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GenresResponse {
    @SerializedName("genres")
    @Expose
    val genres: List<Genre>? = null
}