package com.example.movielopp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.movielopp.R
import com.example.movielopp.fragments.ProfileFragment

class ProfileActivity : AppCompatActivity() {

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
