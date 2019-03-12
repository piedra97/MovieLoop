package com.example.movielopp

interface OnGetMoviesCallBack {
    fun onSuccess(movies: List<Movie>)

    fun onError()
}