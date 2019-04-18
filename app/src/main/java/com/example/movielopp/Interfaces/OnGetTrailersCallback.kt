package com.example.movielopp.Interfaces

import com.example.movielopp.Model.Trailer

interface OnGetTrailersCallback {
    fun onSuccess(trailers:List<Trailer>)

    fun onError()
}