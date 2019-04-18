package com.example.movielopp.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.movielopp.Model.Movie
import com.example.movielopp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.template_grid.view.*

class AdapterPopularMovies(mContext:Context, movies:List<Movie>, val listener: (Int) -> Unit) : RecyclerView.Adapter<AdapterPopularMovies.ViewHolder>() {

    private var mContext: Context
    private var movies: List<Movie> = ArrayList()
    private val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    init {
        this.movies = movies
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = (LayoutInflater.from(parent.context).inflate(R.layout.template_grid, parent, false))
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


        fun bind(movie:Movie, listener: (Int) -> Unit) = with(itemView) {
            Picasso.get().load(IMAGE_BASE_URL + movie.posterPath).into(filmPoster)
            setOnClickListener{
                listener(movie.id)
            }
        }


    }
}

