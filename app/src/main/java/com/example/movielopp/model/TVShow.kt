package com.example.movielopp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TVShow () : Parcelable{
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
    var runtime:List<Int>?= null

    @SerializedName("status")
    @Expose
    var status:String?= null

    @SerializedName("created_by")
    @Expose
    var createdBy:List<Creator>? = null

    constructor(parcel: Parcel) : this() {
        posterPath = parcel.readString()
        id = parcel.readInt()
        name = parcel.readString()
        firstAirDate = parcel.readString()
        rating = parcel.readFloat()
        overview = parcel.readString()
        backdrop = parcel.readString()
        originalLanguage = parcel.readString()
        originalName = parcel.readString()
        numberOfSeasons = parcel.readValue(Int::class.java.classLoader) as? Int
        status = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(posterPath)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(firstAirDate)
        parcel.writeFloat(rating)
        parcel.writeString(overview)
        parcel.writeString(backdrop)
        parcel.writeString(originalLanguage)
        parcel.writeString(originalName)
        parcel.writeValue(numberOfSeasons)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TVShow> {
        override fun createFromParcel(parcel: Parcel): TVShow {
            return TVShow(parcel)
        }

        override fun newArray(size: Int): Array<TVShow?> {
            return arrayOfNulls(size)
        }
    }


}