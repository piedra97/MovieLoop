package com.example.movielopp.interfaces

import com.example.movielopp.model.TVShow

interface OnGetTVShowsCallback {
    fun onSuccess(tvshows: List<TVShow>)

    fun onError()
}