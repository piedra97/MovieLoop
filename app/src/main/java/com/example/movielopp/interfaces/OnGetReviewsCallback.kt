package com.example.movielopp.interfaces

import com.example.movielopp.model.Review



interface OnGetReviewsCallback {
    fun onSuccess(reviews: List<Review>)

    fun onError()
}