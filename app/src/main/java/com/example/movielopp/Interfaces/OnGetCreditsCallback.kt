package com.example.movielopp.Interfaces

import com.example.movielopp.Model.Cast
import com.example.movielopp.Model.Crew

interface OnGetCreditsCallback {
    fun onSuccess(cast: List<Cast>, crew: List<Crew>)

    fun onError()
}