package com.example.movielopp.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TVShow {
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("name")
    @Expose
    var name: String? = null


    @SerializedName("first_air_date")
    @Expose
    var firstAirDate: String? = null

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

    @SerializedName("networks")
    @Expose
    var networks:List<Network>?= null

    @SerializedName("original_language")
    @Expose
    var originalLanguage:String?= null

    @SerializedName("original_name")
    @Expose
    var originalName:String?= null

    @SerializedName("number_of_seasons")
    @Expose
    var numberOfSeasons:Int?= null

    @SerializedName("episode_run_time")
    @Expose
    var runtime:Int?= null

    @SerializedName("status")
    @Expose
    var status:String?= null

    @SerializedName("created_by")
    @Expose
    var createdBy:List<Creator>? = null





}