package com.example.movielopp.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TVShow {
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

}