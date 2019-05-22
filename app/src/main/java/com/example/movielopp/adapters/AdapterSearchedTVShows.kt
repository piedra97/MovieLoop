package com.example.movielopp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movielopp.model.TVShow
import com.example.movielopp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_template_grid.view.*

class AdapterSearchedTVShows(mContext: Context, tvshows:List<TVShow>, val listener: (TVShow) -> Unit) : RecyclerView.Adapter<AdapterSearchedTVShows.ViewHolder>() {

    private var mContext: Context
    private var tvshows: List<TVShow> = ArrayList()
    private val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    init {
        this.tvshows = tvshows
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = (LayoutInflater.from(parent.context).inflate(R.layout.search_template_grid, parent, false))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tvshows.size
    }

    fun setMovies(tvshows:  List<TVShow>) {
        this.tvshows = tvshows
    }

    fun clearMovies() {
        //movies.ArraylIST.CLEAR
        //notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tvshows[position], listener)
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {


        fun bind(tvshow: TVShow, listener: (TVShow) -> Unit) = with(itemView) {
            itemTitle.text = tvshow.name
            Picasso.get().load(IMAGE_BASE_URL + tvshow.backdrop).placeholder(R.drawable.icons8_anonymous_mask_48).into(itemCover)
            setOnClickListener{
                listener(tvshow)
            }
        }


    }
}