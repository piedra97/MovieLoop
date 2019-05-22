package com.example.movielopp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movielopp.model.Movie
import com.example.movielopp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_template_grid.view.*

class AdapterSearchedMovies(mContext:Context, movies:List<Movie>, val listener: (Movie) -> Unit) : RecyclerView.Adapter<AdapterSearchedMovies.ViewHolder>() {

    private var mContext: Context
    private var movies: List<Movie> = ArrayList()
    private val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    init {
        this.movies = movies
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = (LayoutInflater.from(parent.context).inflate(R.layout.search_template_grid, parent, false))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setMovies(movies: List<Movie>) {
        this.movies = movies
    }

    fun clearMovies() {
        //movies.ArraylIST.CLEAR
        //notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position], listener)
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {


        fun bind(movie:Movie, listener: (Movie) -> Unit) = with(itemView) {
            itemTitle.text = movie.title
            Picasso.get().load(IMAGE_BASE_URL + movie.backdrop).placeholder(R.drawable.icons8_anonymous_mask_48).into(itemCover)
            setOnClickListener{
                listener(movie)
            }
        }


    }
}