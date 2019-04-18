package com.example.movielopp.Interfaces

import com.example.movielopp.Model.Genre

interface OnGetGenresCallback {
    fun onSuccess(genres:List<Genre>)

    fun onError()
}