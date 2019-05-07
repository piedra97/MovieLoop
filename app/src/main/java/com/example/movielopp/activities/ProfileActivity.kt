package com.example.movielopp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.movielopp.R
import com.example.movielopp.fragments.*
import com.example.movielopp.model.Movie
import com.example.movielopp.model.TVShow
import com.google.firebase.auth.FirebaseAuth


class ProfileActivity : AppCompatActivity(), ProfileFragment.OnSignOutClicked , ProfileFragment.OnRatingsClicked, ProfileFragment.OnReviewsClicked, ReviewsUserListFragment.OnReviewItemListClicked, ProfileFragment.OnListsClicked, FragmentUserListContainer.OnListMoviesUsersClicked, FragmentChooseListType.OnListMoviesFavClicked, FragmentChooseListType.OnWatchListMovieClicked, FragmentChooseListType.OnListMoviesWatchedClicked, VotesUserListFragment.OnVoteFilmClicked, MovieDetailsFragment.OnReviewFilmClicked, FragmentUsersFilm.OnMoviesClickedListener, VotesUserListFragment.OnVoteTVShowClicked{



    private var fragmentMovieDetails: MovieDetailsFragment? = null

    private var fragmentTVShowDetails: TVShowDetailsFragment? = null

    private var ratingMovieClicked = false

    private var ratingTVShowClicked = false

    private var movieClicked = false

    private var tvShowClicked = false

    override fun onVoteTVShowClicked(tvShow: TVShow) {

        ratingTVShowClicked = true

        fragmentTVShowDetails = TVShowDetailsFragment.newInstance(tvShow)

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentTVShowDetails!!).
            commit()

    }

    override fun onMovieClicked(movie: Movie) {

        movieClicked = true

        fragmentMovieDetails = MovieDetailsFragment.newInstance(movie)

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieDetails!!).
            commit()
    }

    override fun onReviewFilmClicked(movie: Movie) {
        val intent = Intent(this, ReviewMovieActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
        finish()
    }

    override fun onVoteFilmClicked(movie: Movie) {
        ratingMovieClicked = true

        fragmentMovieDetails = MovieDetailsFragment.newInstance(movie)

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieDetails!!).
            commit()
    }

    override fun onReviewItemListClicked(movie: Movie) {
        val fragmentMovieDetails = MovieDetailsFragment.newInstance(movie)

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieDetails).
            commit()
    }

    override fun onListMoviesUsersClicked() {
        val fragmentChooseListType = FragmentChooseListType()

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentChooseListType).
            addToBackStack(null).
            commit()
    }

    override fun onListMoviesFavsClciked() {

        val fragmentMovieFav = FragmentUsersFilm.newInstance("FavoriteMovie")

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieFav).
            addToBackStack(null).
            commit()


    }

    override fun onListMoviesWatchedClicked() {

        val fragmentMovieWatched = FragmentUsersFilm.newInstance("WatchedMovieList")

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieWatched).
            addToBackStack(null).
            commit()

    }

    override fun onWatchListMovieClicked() {

        val fragmentMovieWatchList = FragmentUsersFilm.newInstance("WatchListMovie")

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieWatchList).
            addToBackStack(null).
            commit()

    }

    override fun onListsClicked() {
        val fragmentLists = FragmentUserListContainer()

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentLists).
            addToBackStack(null).
            commit()
    }


    override fun onReviewsClicked() {
        val fragmentReviews = ReviewsUserListFragment()

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentReviews).
            addToBackStack(null).
            commit()
    }

    override fun onRatingsClicked() {
        val fragmentRatings = VotesUserListFragment()

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentRatings).
            addToBackStack(null).
            commit()
    }

    override fun onSignOutClicked() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()

        goToMainActivity()
        finish()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount

        if (ratingMovieClicked) {
            supportFragmentManager.beginTransaction().remove(fragmentMovieDetails!!).commit()
            ratingMovieClicked = false
        }

        if (ratingTVShowClicked) {
            supportFragmentManager.beginTransaction().remove(fragmentTVShowDetails!!).commit()
            ratingTVShowClicked = false
        }

        if (movieClicked) {
            supportFragmentManager.beginTransaction().remove(fragmentMovieDetails!!).commit()
            movieClicked = false
        }

        if (tvShowClicked) {
            supportFragmentManager.beginTransaction().remove(fragmentTVShowDetails!!).commit()
        }

        if (count == 0) {
            super.onBackPressed()
            goToMainActivity()
        } else {
            supportFragmentManager.popBackStack()

        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val fragmentProfile = ProfileFragment()

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentProfile).
            commit()

    }
}

