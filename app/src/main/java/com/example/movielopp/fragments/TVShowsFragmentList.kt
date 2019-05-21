package com.example.movielopp.fragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import com.example.movielopp.adapters.AdapterPopularTVShows
import com.example.movielopp.interfaces.OnGetTVShowsCallback
import com.example.movielopp.model.TVShow
import com.example.movielopp.network.TVShowsRepository

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

    interface OnTVShowsClickedListener {
        fun onTVShowsClicked(tvShow:TVShow)
    }

    private var sortBy = "POPULAR"

    var tvShowsRepository:TVShowsRepository? = null

    var adapterCustom:AdapterPopularTVShows? = null

    lateinit var listenerList: OnTVShowsClickedListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshows_fragment_list, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val sort = menu?.findItem(R.id.sort)
        sort?.isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        menu?.clear()
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureList()
        getSortedTVShows()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun getSortedTVShows() {
        tvShowsRepository = TVShowsRepository.instance
        tvShowsRepository!!.getTVShows(sortBy, object : OnGetTVShowsCallback {
            override fun onSuccess(tvshows: List<TVShow>) {
                adapterCustom = AdapterPopularTVShows(activity?.applicationContext!!, tvshows) {
                    listenerList.onTVShowsClicked(it)
                }
                tvShows_listing.adapter = adapterCustom
                progressLayoutTV.visibility = View.GONE
            }

            override fun onError() {
                Toast.makeText(activity, getString(R.string.internet_condition), Toast.LENGTH_SHORT).show()
            }


        })
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.sort -> {
                showSortMenu()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun showSortMenu() {
        val menuItem = activity?.findViewById<View>(R.id.sort)
        val sortMenu = PopupMenu(context, menuItem, Gravity.END)
        sortMenu.inflate(R.menu.menu_movies_sort)
        sortMenu.show()
        sortMenu.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item!!.itemId) {
                    R.id.popular -> {
                        sortBy = TVShowsRepository.POPULAR
                        getSortedTVShows()
                        return true
                    }
                    R.id.top_rated -> {
                        sortBy = TVShowsRepository.TOP_RATED
                        getSortedTVShows()
                        return true
                    }
                    R.id.upcoming -> {
                        sortBy = TVShowsRepository.UPCOMING
                        getSortedTVShows()
                        return true
                    }
                    else -> return false
                }
            }

        })
    }


    private fun configureList() {
        tvShows_listing.setHasFixedSize(true)
        tvShows_listing.layoutManager = GridLayoutManager(this.context,2)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listenerList = context as OnTVShowsClickedListener
    }


}
