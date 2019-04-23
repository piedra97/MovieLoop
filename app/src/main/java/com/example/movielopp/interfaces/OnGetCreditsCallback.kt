package com.example.movielopp.interfaces

import com.example.movielopp.model.Cast
import com.example.movielopp.model.Crew

interface OnGetCreditsCallback {
    fun onSuccess(cast: List<Cast>, crew: List<Crew>)

    fun onError()
}