package com.example.movielopp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Creator {

    @SerializedName("name")
    @Expose
    var name:String? = null
}