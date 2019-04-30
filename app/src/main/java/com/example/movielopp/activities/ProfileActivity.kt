package com.example.movielopp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.movielopp.R
import com.example.movielopp.fragments.ProfileFragment
import com.example.movielopp.fragments.VotesUserListFragment
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity(), ProfileFragment.OnSignOutClicked , ProfileFragment.OnRatingsClicked{

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

