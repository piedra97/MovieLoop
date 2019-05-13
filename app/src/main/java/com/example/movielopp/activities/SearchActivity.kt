package com.example.movielopp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.movielopp.R
import com.example.movielopp.fragments.MovieDetailsFragment
import com.example.movielopp.fragments.SearchFragment
import com.example.movielopp.fragments.TVShowDetailsFragment
import com.example.movielopp.model.Movie
import com.example.movielopp.model.TVShow

class SearchActivity : AppCompatActivity() ,SearchFragment.OnGetMovieSearchedClicked, SearchFragment.OnGetTVShowSearchedClicked, MovieDetailsFragment.OnReviewFilmClicked, TVShowDetailsFragment.OnReviewTVShowClicked{

    override fun onReviewFilmClicked(movie: Movie) {
        val intent = Intent(this, ReviewMovieActivity::class.java)
        intent.putExtra(getString(R.string.movie_resource), movie)
        startActivity(intent)
        finish()
    }

    override fun onReviewTVShowClicked(tvshow: TVShow) {
        val intent = Intent(this, ReviewTVShowActivity::class.java)
        intent.putExtra(getString(R.string.tv_show_resource), tvshow)
        startActivity(intent)
        finish()

    }

    override fun onMovieSearchedClicked(movie: Movie) {
        val movieDetails = MovieDetailsFragment.newInstance(movie)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_search, movieDetails).
            addToBackStack(null).
            commit()
    }

    override fun onTVShowSearchedClicked(tvShow: TVShow) {
        val tvShowDetails = TVShowDetailsFragment.newInstance(tvShow)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_search, tvShowDetails).
            addToBackStack(null).
            commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val query = intent.getStringExtra(getString(R.string.query_resource))

        val searchFragment = SearchFragment.newInstance(query)

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_search, searchFragment).
            commit()

    }
}
