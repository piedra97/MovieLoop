package com.example.movielopp.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.movielopp.*
import com.example.movielopp.Adapters.AdapterPopularMovies
import com.example.movielopp.Interfaces.OnGetMoviesCallBack
import com.example.movielopp.Model.Movie
import com.example.movielopp.Network.MoviesRepository
import kotlinx.android.synthetic.main.fragment_list_film.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListFilmFragment : Fragment() {

    private var adapterCustom: AdapterPopularMovies? = null

    private var moviesRepository: MoviesRepository? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_film, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureList()
        getSortedMovies()


    }

    private fun getSortedMovies() {
        moviesRepository = MoviesRepository.instance
        moviesRepository!!.getMovies(object : OnGetMoviesCallBack {

        override fun onSuccess(movies: List<Movie>) {
            adapterCustom!!.setMovies(movies)
            adapterCustom!!.notifyDataSetChanged()
        }

        override fun onError() {
            Toast.makeText(activity, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
        })
    }

    private fun configureList() {
        movies_listing.setHasFixedSize(true)
        movies_listing.layoutManager = GridLayoutManager(this.context,2)
        adapterCustom = AdapterPopularMovies(this.context!!, emptyList())
        movies_listing.adapter = adapterCustom
    }


}
