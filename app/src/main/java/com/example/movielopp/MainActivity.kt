package com.example.movielopp
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.movielopp.Fragments.ListFilmFragment


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentListFilms = ListFilmFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, fragmentListFilms).
            commit()


    }
}
