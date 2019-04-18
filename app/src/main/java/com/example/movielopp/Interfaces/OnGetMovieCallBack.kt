package com.example.movielopp.Interfaces

import com.example.movielopp.Model.Movie

interface OnGetMovieCallBack {
    fun onSuccess(movie: Movie)

    fun onError()
}