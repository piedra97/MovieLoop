package com.example.movielopp.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.movielopp.Fragments.ListFilmFragment
import com.example.movielopp.Fragments.TVShowsFragmentList


class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ListFilmFragment()
            }
            else -> {
                return TVShowsFragmentList()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Películas"
            else -> {
                return "Series"
            }
        }
    }
}