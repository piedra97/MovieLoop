package com.example.movielopp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.movielopp.R
import com.example.movielopp.fragments.ProfileFragment
import com.example.movielopp.fragments.ReviewDetailsFragment
import com.example.movielopp.fragments.ReviewsUserListFragment
import com.example.movielopp.fragments.VotesUserListFragment
import com.example.movielopp.model.ModelListReviews
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity(), ProfileFragment.OnSignOutClicked , ProfileFragment.OnRatingsClicked, ProfileFragment.OnReviewsClicked, ReviewsUserListFragment.OnReviewItemListClicked{


    private var movedInFragment = false

    override fun onReviewItemListClicked(reviewClicked: ModelListReviews) {
        val fragmentReviewDetails = ReviewDetailsFragment.newInstance(reviewClicked.movieURL!!, reviewClicked.reviewValue)

        movedInFragment = true
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentReviewDetails).
            addToBackStack(null).
            commit()


    }

    override fun onReviewsClicked() {
        val fragmentReviews = ReviewsUserListFragment()

        movedInFragment = true
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container_profile, fragmentReviews).
            addToBackStack(null).
            commit()
    }

    override fun onRatingsClicked() {
        val fragmentRatings = VotesUserListFragment()

        movedInFragment = true
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
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!movedInFragment) {
            goToMainActivity()
        }

        movedInFragment = false
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

