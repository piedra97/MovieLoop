package com.example.movielopp.activities
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.movielopp.R
import com.example.movielopp.adapters.MyPagerAdapter
import com.example.movielopp.fragments.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),LoginFragment.OnButtonLoginPressedListener, LoginFragment.OnTextRegistredPressedListener, RegisterFragment.OnGoToLoginPressed, RegisterFragment.OnRegistrationConfirmPressed, ListFilmFragment.OnMoviesClickedListener{

    override fun onMovieClicked(iDMovie: Int) {
        /*val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("idMovie", iDMovie)
        startActivity(intent)*/
        val movieDetails = MovieDetailsFragment.newInstance(iDMovie)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, movieDetails).
            addToBackStack(null).
            commit()
    }


    override fun onLoginPressed() {
        val listFragmenfilm = ListFilmFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, listFragmenfilm).
            commit()
    }

    override fun onRegistrationConfirmPressed() {
        val loginFragment = LoginFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, loginFragment).
            commit()

    }

    override fun onGoToLoginPressed() {
        val loginFragment = LoginFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, loginFragment).
            commit()
    }


    override fun onRegisteredPressed(username: String) {
        val fragmentRegister = RegisterFragment.newInstance(username)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, fragmentRegister).
            commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStatePageAdapter()


        /*val fragmentListFilms = ListFilmFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, fragmentListFilms).
            commit()*/
    }

    private fun setStatePageAdapter() {

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter

        tabLayout.setupWithViewPager(viewPager)

    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val authUser = FirebaseAuth.getInstance().signOut()
        val profileItem = menu?.findItem(R.id.profile)
        val signInItem = menu?.findItem(R.id.signInButton)
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            profileItem?.isVisible = true
            signInItem?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.signInButton -> {
                val signInFragment = LoginFragment()
                supportFragmentManager.
                    beginTransaction().
                    replace(R.id.main_container, signInFragment).
                    commit()

                true

            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}
