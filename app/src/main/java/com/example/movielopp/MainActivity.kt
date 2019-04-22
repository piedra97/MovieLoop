package com.example.movielopp
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.movielopp.Adapters.TabAdapter
import com.example.movielopp.Fragments.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),LoginFragment.OnButtonLoginPressedListener, LoginFragment.OnTextRegistredPressedListener, RegisterFragment.OnGoToLoginPressed, RegisterFragment.OnRegistrationConfirmPressed, ListFilmFragment.OnMoviesClickedListener{

    override fun onMovieClicked(iDMovie: Int) {
        val movieDetailsFragment = MovieDetailsFragment.newInstance(iDMovie)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, movieDetailsFragment).
            addToBackStack(null).
            commit()
        supportFragmentManager.addOnBackStackChangedListener {
        }
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

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                val fm = supportFragmentManager
                val ft = fm.beginTransaction()
                val count = fm.backStackEntryCount
                if (count >= 1) {
                    supportFragmentManager.popBackStack()
                }
                ft.commit()
            }

        })

        val auth = FirebaseAuth.getInstance().signOut()


        /*val fragmentListFilms = ListFilmFragment()
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, fragmentListFilms).
            commit()*/
    }

    private fun setStatePageAdapter() {

        val adapter = TabAdapter(supportFragmentManager)

        adapter.addFragment(ListFilmFragment(), "Películas")
        adapter.addFragment(TVShowsFragmentList(), "Series")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager, true)

    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
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
