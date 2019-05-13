package com.example.movielopp.activities
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.movielopp.R
import com.example.movielopp.adapters.MyPagerAdapter
import com.example.movielopp.fragments.*
import com.example.movielopp.model.Movie
import com.example.movielopp.model.TVShow
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list_film.*
import kotlinx.android.synthetic.main.fragment_tvshows_fragment_list.*

class MainActivity : AppCompatActivity(), ListFilmFragment.OnMoviesClickedListener, TVShowsFragmentList.OnTVShowsClickedListener, MovieDetailsFragment.OnReviewFilmClicked, TVShowDetailsFragment.OnReviewTVShowClicked{


    private var movieClicked = false

    private var tvShowCliked = false

    private var movieDetails :MovieDetailsFragment? = null

    private var tvShowDetails: TVShowDetailsFragment? = null

    override fun onReviewTVShowClicked(tvshow: TVShow) {
        val intent = Intent(this, ReviewTVShowActivity::class.java)
        intent.putExtra(getString(R.string.tv_show_resource), tvshow)
        startActivity(intent)
        finish()

    }

    override fun onReviewFilmClicked(movie: Movie) {

        val intent = Intent(this, ReviewMovieActivity::class.java)
        intent.putExtra(getString(R.string.movie_resource), movie)
        startActivity(intent)
        finish()

    }

    override fun onTVShowsClicked(tvShow: TVShow) {
        tvShowCliked = true
        progressLayoutTV.visibility = View.VISIBLE
        tvShowDetails = TVShowDetailsFragment.newInstance(tvShow)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, tvShowDetails!!).
            addToBackStack(null).
            commit()
    }


    override fun onMovieClicked(movie:Movie) {

        movieClicked = true
        progressLayout.visibility = View.VISIBLE
        movieDetails = MovieDetailsFragment.newInstance(movie)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, movieDetails!!).
            addToBackStack(null).
            commit()
    }


    override fun onBackPressed() {
        if (movieDetails != null || tvShowDetails != null) {
            if (movieDetails != null) {
                if (movieDetails!!.allowBackPressed()) {
                    super.onBackPressed()
                    progressLayout.visibility = View.GONE
                    progressLayoutTV.visibility = View.GONE
                    movieClicked = false
                }
            } else if (tvShowDetails!!.allowBackPressed()) {
                super.onBackPressed()
                progressLayout.visibility = View.GONE
                progressLayoutTV.visibility = View.GONE
                tvShowCliked = false
            }
        } else {
            super.onBackPressed()
        }

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStatePageAdapter()


        //val toolbarToWork = findViewById<Toolbar>(R.id.toolbar)
        //setSupportActionBar(toolbarToWork)

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
        val profileItem = menu?.findItem(R.id.profile)
        val signInItem = menu?.findItem(R.id.signInButton)
        val sortItem = menu?.findItem(R.id.sort)
        val auth = FirebaseAuth.getInstance()
        if (movieClicked || tvShowCliked) {
            profileItem?.isVisible = false
            signInItem?.isVisible = false
            sortItem?.isVisible = false
        }
        if (auth.currentUser != null) {
            profileItem?.isVisible = true
            signInItem?.isVisible = false
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.signInButton -> {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true

            }
            R.id.searchItem -> {
                val searchView = item.actionView as SearchView

                searchView.queryHint = getString(R.string.int_busqueda)

                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(p0: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val intent = Intent(this@MainActivity, SearchActivity::class.java)
                        intent.putExtra(getString(R.string.query_resource),query)
                        startActivity(intent)
                        return true
                    }

                })

                true


            }
            R.id.profile -> {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
                finish()
                true

            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

}
