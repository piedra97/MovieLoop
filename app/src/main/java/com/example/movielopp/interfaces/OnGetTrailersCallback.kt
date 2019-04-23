package com.example.movielopp.interfaces

import com.example.movielopp.model.Trailer

interface OnGetTrailersCallback {
    fun onSuccess(trailers:List<Trailer>)

    fun onError()
}