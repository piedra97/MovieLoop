package com.example.movielopp.Interfaces

import com.example.movielopp.Model.TVShow

interface OnGetTVShowsCallback {
    fun onSuccess(tvshows: List<TVShow>)

    fun onError()
}