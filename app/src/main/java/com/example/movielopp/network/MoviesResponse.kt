package com.example.movielopp.network

import com.example.movielopp.model.Movie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MoviesResponse {


    @SerializedName("results")
    @Expose
    var movies: List<Movie>? = null

}