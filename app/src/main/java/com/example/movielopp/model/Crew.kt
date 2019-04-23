package com.example.movielopp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Crew {
    @SerializedName("job")
    @Expose
    var job:String? = null
    @SerializedName("name")
    @Expose
    var name:String? = null
    @SerializedName("profile_path")
    @Expose
    var profile_path:String? = null
}