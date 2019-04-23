package com.example.movielopp.Network

import com.example.movielopp.Model.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MoviesResponse {


    @SerializedName("results")
    @Expose
    var movies: List<Movie>? = null

}