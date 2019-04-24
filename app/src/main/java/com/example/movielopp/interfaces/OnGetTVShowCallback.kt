package com.example.movielopp.interfaces

import com.example.movielopp.model.TVShow


interface OnGetTVShowCallback {

    fun onSuccess(tvshow: TVShow)

    fun onError()
}