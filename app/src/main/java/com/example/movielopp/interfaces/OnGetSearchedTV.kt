package com.example.movielopp.interfaces

import com.example.movielopp.model.TVShow

interface OnGetSearchedTV {
    fun onSuccess(tvShows: List<TVShow>)

    fun onError()
}