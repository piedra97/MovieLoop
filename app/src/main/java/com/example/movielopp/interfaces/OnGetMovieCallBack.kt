package com.example.movielopp.interfaces

import com.example.movielopp.model.Movie

interface OnGetMovieCallBack {
    fun onSuccess(movie: Movie)

    fun onError()
}