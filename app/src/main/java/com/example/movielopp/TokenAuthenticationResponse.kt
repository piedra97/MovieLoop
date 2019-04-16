package com.example.movielopp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TokenAuthenticationResponse {
    @SerializedName("success")
    @Expose
    var success:Boolean = false

}
