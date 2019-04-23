package com.example.movielopp.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.Toast
import com.example.movielopp.Adapters.AdapterPopularTVShows
import com.example.movielopp.Interfaces.OnGetTVShowsCallback
import com.example.movielopp.Model.TVShow
import com.example.movielopp.Network.TVShowsRepository

import com.example.movielopp.R
import kotlinx.android.synthetic.main.fragment_tvshows_fragment_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TVShowsFragmentList : Fragment() {

    var tvShowsRepository:TVShowsRepository? = null
    var adapterCustom:AdapterPopularTVShows? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tvshows_fragment_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater){
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureList()
        getSortedMovies()
    }

    private fun getSortedMovies() {
        tvShowsRepository = TVShowsRepository.instance
        tvShowsRepository!!.getTVShow(object : OnGetTVShowsCallback {
            override fun onSuccess(tvshows: List<TVShow>) {
                adapterCustom = AdapterPopularTVShows(context!!, tvshows)
                tvShows_listing.adapter = adapterCustom
                listTVShows_progressBar.visibility = View.GONE
            }

            override fun onError() {
                Toast.makeText(activity, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
            }


        })
    }

    private fun configureList() {
        tvShows_listing.setHasFixedSize(true)
        tvShows_listing.layoutManager = GridLayoutManager(this.context,2)
    }


}
