package com.example.movielopp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.movielopp.R
import kotlinx.android.synthetic.main.fragment_choose_list_type.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FragmentChooseListType : Fragment() {

    interface OnListMoviesFavClicked {
        fun onListMoviesFavsClciked()
    }

    interface OnListMoviesWatchedClicked {
        fun onListMoviesWatchedClicked()
    }

    interface OnWatchListMovieClicked {
        fun onWatchListMovieClicked()
    }

    private lateinit var listenerFavoriteMovies: OnListMoviesFavClicked

    private lateinit var listenerWatchedMovies: OnListMoviesWatchedClicked

    private lateinit var listenerWatchListMovies: OnWatchListMovieClicked


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_list_type, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoriteMovies.setOnClickListener {
            listenerFavoriteMovies.onListMoviesFavsClciked()
        }

        watchedMovies.setOnClickListener {
            listenerWatchedMovies.onListMoviesWatchedClicked()
        }

        moviesWatchList.setOnClickListener {
            listenerWatchListMovies.onWatchListMovieClicked()
        }


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listenerFavoriteMovies = context as OnListMoviesFavClicked
        listenerWatchedMovies = context as OnListMoviesWatchedClicked
        listenerWatchListMovies = context as OnWatchListMovieClicked
    }


}
