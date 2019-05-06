package com.example.movielopp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movielopp.R
import com.example.movielopp.model.Movie
import com.example.movielopp.model.MovieToList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.template_grid.view.*

class AdapterMovieInList(mContext: Context, moviesInList:List<MovieToList>, val listener: (Movie) -> Unit) : RecyclerView.Adapter<AdapterMovieInList.ViewHolder>() {

    private var mContext: Context
    private var moviesInList: List<MovieToList> = ArrayList()
    private val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    init {
        this.moviesInList = moviesInList
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = (LayoutInflater.from(parent.context).inflate(R.layout.template_grid, parent, false))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesInList.size
    }

    fun setMovies(moviesInList: List<MovieToList>) {
        this.moviesInList = moviesInList
    }

    fun clearMovies() {
        //movies.ArraylIST.CLEAR
        //notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(moviesInList[position], listener)
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {


        fun bind(movieInList: MovieToList, listener: (Movie) -> Unit) = with(itemView) {
            Picasso.get().load(IMAGE_BASE_URL + movieInList.movie?.posterPath).into(filmPoster)
            setOnClickListener{
                listener(movieInList.movie!!)
            }
        }


    }
}
