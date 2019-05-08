package com.example.movielopp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.movielopp.R
import kotlinx.android.synthetic.main.fragment_choose_list_type.*
import kotlinx.android.synthetic.main.fragment_fragment_choose_tvshow_type.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FragmentChooseTVShowListType : Fragment() {

    interface OnListTVShowFavClicked {
        fun onListTVShowFavsClicked()
    }

    interface OnListTVShowWatchedClicked {
        fun onListTVShowWatchedClicked()
    }

    interface OnWatchListTVShowClicked {
        fun onWatchListTVShowClicked()
    }

    private lateinit var listenerFavoriteTVShow: OnListTVShowFavClicked

    private lateinit var listenerWatchedTVShow: OnListTVShowWatchedClicked

    private lateinit var listenerWatchListTVShow: OnWatchListTVShowClicked


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_choose_tvshow_type, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoriteTVShows.setOnClickListener {
            listenerFavoriteTVShow.onListTVShowFavsClicked()
        }

        watchedTVShow.setOnClickListener {
            listenerWatchedTVShow.onListTVShowWatchedClicked()
        }

        TVShowsWatchList.setOnClickListener {
            listenerWatchListTVShow.onWatchListTVShowClicked()
        }


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listenerFavoriteTVShow = context as OnListTVShowFavClicked
        listenerWatchedTVShow = context as OnListTVShowWatchedClicked
        listenerWatchListTVShow = context as OnWatchListTVShowClicked
    }


}
