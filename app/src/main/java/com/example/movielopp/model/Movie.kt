package com.example.movielopp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Movie {

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null

    @SerializedName("vote_average")
    @Expose
    var rating: Float = 0.toFloat()

    @SerializedName("overview")
    @Expose
    var overview:String? = null

    @SerializedName("backdrop_path")
    @Expose
    var backdrop:String? = null

    @SerializedName("genres")
    @Expose
    var genres:List<Genre>? = null

    @SerializedName("budget")
    @Expose
    var budget:Int?= null

    @SerializedName("original_language")
    @Expose
    var originalLanguage:String?= null

    @SerializedName("original_title")
    @Expose
    var originalTitle:String?= null

    @SerializedName("revenue")
    @Expose
    var revenue:Int?= null

    @SerializedName("runtime")
    @Expose
    var runtime:Int?= null

    @SerializedName("status")
    @Expose
    var status:String?= null

}