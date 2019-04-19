package com.example.movielopp.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Review {
    @SerializedName("author")
    @Expose
    val author: String? = null

    @SerializedName("content")
    @Expose
    val content: String? = null

}