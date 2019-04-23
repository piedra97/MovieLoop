package com.example.movielopp.network

import com.example.movielopp.model.TVShow
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TVShowsResponse {
    @SerializedName("results")
    @Expose
    var tvShows: List<TVShow>? = null
}