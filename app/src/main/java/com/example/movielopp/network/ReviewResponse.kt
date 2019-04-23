package com.example.movielopp.network

import com.example.movielopp.model.Review
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ReviewResponse {
    @SerializedName("results")
    @Expose
    val reviews: List<Review>? = null
}