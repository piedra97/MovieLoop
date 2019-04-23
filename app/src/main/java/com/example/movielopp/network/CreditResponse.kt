package com.example.movielopp.network

import com.example.movielopp.model.Cast
import com.example.movielopp.model.Crew
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreditResponse {
    @SerializedName("cast")
    @Expose
    var cast:List<Cast> ? = null
    @SerializedName("crew")
    @Expose
    var crew:List<Crew> ? = null
}