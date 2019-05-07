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

class MainActivity : AppCompatActivity(), ListFilmFragment.OnMoviesClickedListener, TVShowsFragmentList.OnTVShowsClickedListener, MovieDetailsFragment.OnReviewFilmClicked, TVShowDetailsFragment.OnReviewTVShowClicked{


    private var movieClicked = false

    private var tvShowCliked = false

    override fun onReviewTVShowClicked(tvshow: TVShow) {
        val intent = Intent(this, ReviewTVShowActivity::class.java)
        intent.putExtra("tvShow", tvshow)
        startActivity(intent)
        finish()

    }

    override fun onReviewFilmClicked(movie: Movie) {

        val intent = Intent(this, ReviewMovieActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
        finish()

        /*val reviewMovieFragment = ReviewMovieFragment.newInstance(movie)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, reviewMovieFragment).
            addToBackStack(null).
            commit()*/
    }

    override fun onTVShowsClicked(tvShow: TVShow) {
        tvShowCliked = true
        val tvShowDetails = TVShowDetailsFragment.newInstance(tvShow)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, tvShowDetails).
            addToBackStack(null).
            commit()
    }


    override fun onMovieClicked(movie:Movie) {

        movieClicked = true
        progressLayout.visibility = View.VISIBLE
        val movieDetails = MovieDetailsFragment.newInstance(movie)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, movieDetails).
            addToBackStack(null).
            commit()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        progressLayout.visibility = View.GONE
        movieClicked = false
        tvShowCliked = false

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

                searchView.queryHint = "Introduce tu búsqueda..."

                searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(p0: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val intent = Intent(this@MainActivity, SearchActivity::class.java)
                        intent.putExtra("query",query)
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
