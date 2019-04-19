package com.example.movielopp.Network

import com.example.movielopp.Model.Review
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ReviewResponse {
    @SerializedName("results")
    @Expose
    val reviews: List<Review>? = null
}