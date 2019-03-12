package com.example.movielopp
import android.widget.Toast
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var moviesList: RecyclerView? = null
    private var adapterCustom: AdapterPopularMovies? = null

    private var moviesRepository: MoviesRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moviesRepository = MoviesRepository.instance

        moviesList = findViewById(R.id.movies_listing)

        movies_listing.setHasFixedSize(true)
        movies_listing.layoutManager = GridLayoutManager(this@MainActivity,2)


        moviesRepository!!.getMovies(object : OnGetMoviesCallBack {

            override fun onSuccess(movies: List<Movie>) {
                adapterCustom = AdapterPopularMovies(this@MainActivity,movies)
                moviesList!!.adapter = adapterCustom
            }

            override fun onError() {
                Toast.makeText(this@MainActivity, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
