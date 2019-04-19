package com.example.movielopp.Interfaces

import com.example.movielopp.Model.Review



interface OnGetReviewsCallback {
    fun onSuccess(reviews: List<Review>)

    fun onError()
}