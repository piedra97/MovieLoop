package com.example.movielopp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.movielopp.R
import com.example.movielopp.fragments.MovieDetailsFragment
import com.example.movielopp.fragments.SearchFragment
import com.example.movielopp.fragments.TVShowDetailsFragment

class SearchActivity : AppCompatActivity() ,SearchFragment.OnGetMovieSearchedClicked, SearchFragment.OnGetTVShowSearchedClicked{

    override fun onMovieSearchedClicked(movieID: Int) {
        val movieDetails = MovieDetailsFragment.newInstance(movieID)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_search, movieDetails).
            addToBackStack(null).
            commit()
    }

    override fun onTVShowSearchedClicked(tvShowID: Int) {
        val tvShowDetails = TVShowDetailsFragment.newInstance(tvShowID)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_search, tvShowDetails).
            addToBackStack(null).
            commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val query = intent.getStringExtra("query")

        val searchFragment = SearchFragment.newInstance(query)

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_search, searchFragment).
            addToBackStack(null).
            commit()

    }
}
