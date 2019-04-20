package com.example.movielopp.Network

import com.example.movielopp.Model.Cast
import com.example.movielopp.Model.Crew
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