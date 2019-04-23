package com.example.movielopp.interfaces

import com.example.movielopp.model.Genre

interface OnGetGenresCallback {
    fun onSuccess(genres:List<Genre>)

    fun onError()
}