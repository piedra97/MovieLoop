package com.example.movielopp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Review(author: String? , content: String?) {


    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("content")
    @Expose
    var content: String? = null

    init {
        this.author = author
        this.content = content
    }

}