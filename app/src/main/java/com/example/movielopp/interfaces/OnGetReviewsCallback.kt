package com.example.movielopp.interfaces

import com.example.movielopp.model.Review



interface OnGetReviewsCallback {
    fun onSuccess(reviews: ArrayList<Review>)

    fun onError()
}