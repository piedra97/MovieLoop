package com.example.movielopp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.movielopp.R
import kotlinx.android.synthetic.main.fragment_user_list_container.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FragmentUserListContainer : Fragment() {

    interface OnListMoviesUsersClicked {
        fun onListMoviesUsersClicked()
    }

    interface OnListTVShowsusersClciked {
        fun onListTVShowsUsersClicked()
    }

    private lateinit var listenerMoviesUserList: OnListMoviesUsersClicked

   private lateinit var listenerTVShowsUserlIST: OnListTVShowsusersClciked

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_list_container, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        moviesUser.setOnClickListener {
            listenerMoviesUserList.onListMoviesUsersClicked()
        }

        tvShowsUser.setOnClickListener {
            listenerTVShowsUserlIST.onListTVShowsUsersClicked()
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listenerMoviesUserList = context as OnListMoviesUsersClicked
        listenerTVShowsUserlIST = context as OnListTVShowsusersClciked
    }


}
