package com.example.movielopp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.movielopp.R
import com.example.movielopp.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_review_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ReviewDetailsFragment : Fragment() {

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"

    private var urlImage:String? = null

    private var review:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urlImage = arguments!!.getString("url")
        review = arguments!!.getString("review")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setComponents()
    }

    private fun setComponents() {
        Picasso.
            get().
            load(IMAGE_BASE_URL + urlImage).
            placeholder(R.drawable.ic_launcher_foreground).
            into(moviePoster)
        reviewContent.text = review
    }

    companion object {
        fun newInstance(urlPath:String, reviewContent:String): MovieDetailsFragment{
            val fragmentMovieDetail = MovieDetailsFragment()
            val args = Bundle()

            args.putString("url", urlPath)
            args.putString("review", reviewContent)
            fragmentMovieDetail.arguments = args

            return fragmentMovieDetail
        }
    }


}
