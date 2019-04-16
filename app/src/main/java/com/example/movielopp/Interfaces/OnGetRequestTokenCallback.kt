package com.example.movielopp.Interfaces


interface OnGetRequestTokenCallback {
    fun onSuccess(request_token:String)

    fun onError()
}