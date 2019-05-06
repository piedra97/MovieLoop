package com.example.movielopp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.view.View
import com.example.movielopp.R
import com.example.movielopp.fragments.*
import com.example.movielopp.model.Movie
import com.google.firebase.auth.FirebaseAuth


class ProfileActivity : AppCompatActivity(), ProfileFragment.OnSignOutClicked , ProfileFragment.OnRatingsClicked, ProfileFragment.OnReviewsClicked, ReviewsUserListFragment.OnReviewItemListClicked, ProfileFragment.OnListsClicked, FragmentUserListContainer.OnListMoviesUsersClicked, FragmentChooseListType.OnListMoviesFavClicked, FragmentChooseListType.OnWatchListMovieClicked, FragmentChooseListType.OnListMoviesWatchedClicked, VotesUserListFragment.OnVoteFilmClicked, MovieDetailsFragment.OnReviewFilmClicked{

    private var fragmentMovieFav: FragmentUsersFilm? = null

    private var fragmentMovieWatched: FragmentUsersFilm? = null

    private var fragmentMovieWatchList: FragmentUsersFilm? = null

    private var fragmentMovieDetails: MovieDetailsFragment? = null

    private var listFavClicked = false

    private var listWatchedClicked = false

    private var listWatchListClicked = false

    private var ratingClicked = false

    override fun onReviewFilmClicked(movie: Movie) {
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
        finish()
    }

    override fun onVoteFilmClicked(movie: Movie) {
        ratingClicked = true

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
        listFavClicked = true

        fragmentMovieFav = FragmentUsersFilm.newInstance("FavoriteMovie")

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieFav!!).
            addToBackStack(null).
            commit()


    }

    override fun onListMoviesWatchedClicked() {
        listWatchListClicked = true

        fragmentMovieWatched = FragmentUsersFilm.newInstance("WatchedMovieList")

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieWatched!!).
            addToBackStack(null).
            commit()

    }

    override fun onWatchListMovieClicked() {
        listWatchedClicked = true

        fragmentMovieWatchList = FragmentUsersFilm.newInstance("WatchListMovie")

        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentMovieWatchList!!).
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

        if (ratingClicked) {
           // val layoutDetails = findViewById<CoordinatorLayout>(R.id.fragmentDetailsLayout)
            //layoutDetails.visibility = View.GONE
            supportFragmentManager.beginTransaction().remove(fragmentMovieDetails!!).commit()
            ratingClicked = false
        }

        if (count == 0) {
            super.onBackPressed()

            goToMainActivity()
        } else {

           /* if (listFavClicked) {
                supportFragmentManager.beginTransaction().remove(fragmentMovieFav!!).commit()
                listFavClicked = false
            } else if(listWatchListClicked) {
                supportFragmentManager.beginTransaction().remove(fragmentMovieWatchList!!).commit()
                listWatchListClicked = false
            } else if(listWatchedClicked) {
                supportFragmentManager.beginTransaction().remove(fragmentMovieWatched!!).commit()
                listWatchedClicked = false
            }*/
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

