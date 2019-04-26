package com.example.movielopp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.movielopp.R
import com.example.movielopp.adapters.AdapterSearchedMovies
import com.example.movielopp.adapters.AdapterSearchedTVShows
import com.example.movielopp.interfaces.OnGetSearchedMovies
import com.example.movielopp.interfaces.OnGetSearchedTV
import com.example.movielopp.model.Movie
import com.example.movielopp.model.TVShow
import com.example.movielopp.network.MoviesRepository
import com.example.movielopp.network.TVShowsRepository
import kotlinx.android.synthetic.main.fragment_search.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFragment : Fragment() {

    interface OnGetMovieSearchedClicked {
        fun onMovieSearchedClicked(movieID:Int)
    }

    interface OnGetTVShowSearchedClicked {
        fun onTVShowSearchedClicked(tvShowID:Int)
    }

    lateinit var query:String
    lateinit var listenerMoviesList :OnGetMovieSearchedClicked
    lateinit var listenerTVShowList: OnGetTVShowSearchedClicked

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureMoviesList()
        configureTVShowsList()

        getSearchedMovies()
        getSearchedTVShows()
    }

    private fun getSearchedTVShows() {
        val tvshowsRepository = TVShowsRepository.instance
        tvshowsRepository.getSearchedTVShow(query, object : OnGetSearchedTV {
            override fun onSuccess(tvShows: List<TVShow>) {
                val adapterCustomTVShow = AdapterSearchedTVShows(context!!, tvShows) {
                    listenerTVShowList.onTVShowSearchedClicked(it)
                }

                searched_tvshows_list.adapter = adapterCustomTVShow
            }

            override fun onError() {
                showError()
            }

        })
    }

    private fun getSearchedMovies() {
        val moviesRepository = MoviesRepository.instance
        moviesRepository.getSearchedMovie(query, object : OnGetSearchedMovies {
            override fun onSuccess(movies: List<Movie>) {
                val adapterCustomMovies = AdapterSearchedMovies(context!!, movies) {
                    listenerMoviesList.onMovieSearchedClicked(it)
                }

                searched_movies_list.adapter = adapterCustomMovies
                itemsSearched_progressBar.visibility = View.GONE
            }

            override fun onError() {
                showError()
            }

        })
    }

    private fun configureTVShowsList() {
        searched_tvshows_list.setHasFixedSize(true)
        searched_tvshows_list.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun configureMoviesList() {
        searched_movies_list.setHasFixedSize(true)
        searched_movies_list.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun showError() {
        Toast.makeText(context!!, "Comprueba tu conexión a Internet", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = arguments!!.getString("query")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listenerMoviesList = context as OnGetMovieSearchedClicked
        listenerTVShowList = context as OnGetTVShowSearchedClicked
    }

    companion object {
        fun newInstance(query: String): SearchFragment{
            val fragmentSearch = SearchFragment()
            val args = Bundle()

            args.putString("query", query)
            fragmentSearch.arguments = args

            return fragmentSearch
        }
    }

}
