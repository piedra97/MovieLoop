package com.example.movielopp.Network

import com.example.movielopp.Model.TVShow
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TVShowsResponse {
    @SerializedName("results")
    @Expose
    var tvShows: List<TVShow>? = null
}