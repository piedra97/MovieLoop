package com.example.movielopp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Movie() : Parcelable{

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
    var revenue:Long?= null

    @SerializedName("runtime")
    @Expose
    var runtime:Int?= null

    @SerializedName("status")
    @Expose
    var status:String?= null

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString()
        posterPath = parcel.readString()
        releaseDate = parcel.readString()
        rating = parcel.readFloat()
        overview = parcel.readString()
        backdrop = parcel.readString()
        budget = parcel.readValue(Int::class.java.classLoader) as? Int
        originalLanguage = parcel.readString()
        originalTitle = parcel.readString()
        revenue = parcel.readValue(Long::class.java.classLoader) as? Long
        runtime = parcel.readValue(Int::class.java.classLoader) as? Int
        status = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(posterPath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }


}