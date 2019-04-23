package com.example.movielopp.interfaces

import com.example.movielopp.model.Movie

interface OnGetMoviesCallBack {
    fun onSuccess(movies: List<Movie>)

    fun onError()
}