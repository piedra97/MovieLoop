package com.example.movielopp.Network

import com.example.movielopp.Model.Genre
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GenresResponse {
    @SerializedName("genres")
    @Expose
    val genres: List<Genre>? = null
}