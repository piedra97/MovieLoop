package com.example.movielopp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movielopp.R
import com.example.movielopp.model.MovieToList
import com.example.movielopp.model.TVShow
import com.example.movielopp.model.TVShowToList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.template_grid.view.*

class AdapterTVShowInList(mContext: Context, tvShowsInList:List<TVShowToList>, val listener: (TVShow) -> Unit) : RecyclerView.Adapter<AdapterTVShowInList.ViewHolder>() {

    private var mContext: Context
    private var tvShowsInList: List<TVShowToList> = ArrayList()
    private val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    init {
        this.tvShowsInList = tvShowsInList
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = (LayoutInflater.from(parent.context).inflate(R.layout.template_grid, parent, false))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tvShowsInList.size
    }

    fun setTVShows(tvShowsInList: List<TVShowToList>) {
        this.tvShowsInList = tvShowsInList
    }

    fun clearMovies() {
        //movies.ArraylIST.CLEAR
        //notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tvShowsInList[position], listener)
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {


        fun bind(tvShowsInList: TVShowToList, listener: (TVShow) -> Unit) = with(itemView) {
            Picasso.get().load(IMAGE_BASE_URL + tvShowsInList.tvShow?.posterPath).into(filmPoster)
            setOnClickListener{
                listener(tvShowsInList.tvShow!!)
            }
        }


    }
}
