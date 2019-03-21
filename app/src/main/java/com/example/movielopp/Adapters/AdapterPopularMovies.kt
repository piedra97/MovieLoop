package com.example.movielopp.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movielopp.Model.Movie
import com.example.movielopp.R

class AdapterPopularMovies(mContext:Context, movies:List<Movie>) : RecyclerView.Adapter<AdapterPopularMovies.ViewHolder>() {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(mContext).
            load(IMAGE_BASE_URL + movies[position].posterPath).
            apply(RequestOptions.placeholderOf(R.color.colorPrimary)).
            into(holder.filmPoster!!)
    }

    inner class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        var filmPoster: ImageView? = null
        init {
            filmPoster = root.findViewById(R.id.filmPoster)
        }
    }
}

