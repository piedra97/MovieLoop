package com.example.movielopp.Interfaces

import com.example.movielopp.Model.Movie

interface OnGetMoviesCallBack {
    fun onSuccess(movies: List<Movie>)

    fun onError()
}