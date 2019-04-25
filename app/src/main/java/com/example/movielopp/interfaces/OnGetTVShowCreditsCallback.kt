package com.example.movielopp.interfaces

import com.example.movielopp.model.Cast


interface OnGetTVShowCreditsCallback {

    fun onSuccess(cast: List<Cast>)

    fun onError()
}