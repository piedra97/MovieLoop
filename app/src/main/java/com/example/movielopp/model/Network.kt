package com.example.movielopp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Network {
    @SerializedName("name")
    @Expose
    var name:String? = null

    @SerializedName("logo_path")
    @Expose
    var logoPath:String? = null

}