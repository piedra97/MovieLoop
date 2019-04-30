package com.example.movielopp.activities
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.example.movielopp.R
import com.example.movielopp.adapters.MyPagerAdapter
import com.example.movielopp.fragments.*
import com.example.movielopp.model.Movie
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListFilmFragment.OnMoviesClickedListener, TVShowsFragmentList.OnTVShowsClickedListener{

    var movieClicked = false

    var tvShowCliked = false

    override fun onTVShowsClicked(iDTVShow: Int) {
        tvShowCliked = true
        val tvShowDetails = TVShowDetailsFragment.newInstance(iDTVShow)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, tvShowDetails).
            addToBackStack(null).
            commit()
    }


    override fun onMovieClicked(movie:Movie) {

        movieClicked = true
        val movieDetails = MovieDetailsFragment.newInstance(movie)
        supportFragmentManager.
            beginTransaction().
            replace(R.id.main_container, movieDetails).
            addToBackStack(null).
            commit()
    }


    override fun onBackPressed() {
        super.onBackPressed()
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
