package com.example.movielopp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView

import com.example.movielopp.R
import com.example.movielopp.adapters.AdapterMovieReview
import com.example.movielopp.adapters.AdapterTVShowReview
import com.example.movielopp.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ReviewsUserListFragment : Fragment() {

    interface OnMovieReviewItemListClicked {
        fun onMovieReviewItemListClicked(movie: Movie)
    }

    interface OnTVShowReviewItemListClicked {
        fun ontvshowReviewItemListClicked(tvShow: TVShow)
    }

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"

    private var listMovieReviews:ArrayList<ModelMovieListReviews> = ArrayList()

    private var listTVShowReviews:ArrayList<ModelTVShowListReviews> = ArrayList()

    private var adapterMovieReview: AdapterMovieReview? = null

    private var adapterTVShowReview: AdapterTVShowReview? = null

    private lateinit var listenerMovieReviewClicked : OnMovieReviewItemListClicked

    private lateinit var listenerTVShowReviewClicked : OnTVShowReviewItemListClicked

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews_user_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserMovieReviews()
        getTVShowsReviews()

    }


    private fun getTVShowsReviews() {
        val auth = FirebaseAuth.getInstance()
        val currentUserUID = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("ReviewTVShow")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userReviewIT = it.getValue(UserTVShowReview::class.java)
                        userReviewIT?.let {
                            if (userReviewIT.userUID == currentUserUID) {
                                if (!chechIfTVShowIsAlreadyReviewed(userReviewIT))
                                    listTVShowReviews.add(ModelTVShowListReviews(userReviewIT.uidReview, userReviewIT.tvShow, userReviewIT.review, userReviewIT.userUID))
                            }
                        }
                    }
                    if(listTVShowReviews.isEmpty()) {
                        val noDataTextView = activity?.findViewById<TextView>(R.id.noReviewTVShowText)
                        noDataTextView?.visibility = View.VISIBLE
                    } else {
                        val listViewReviews = activity?.findViewById<ListView>(R.id.reviewsUserTVShow_list)
                        configureTVShowsList(listViewReviews)
                        setTVShowsListener(listViewReviews)
                    }
                }
                else {
                    val noDataTextView = activity?.findViewById<TextView>(R.id.noReviewTVShowText)
                    noDataTextView?.visibility = View.VISIBLE
                }
            }

        })


    }

    private fun setTVShowsListener(listViewReviews: ListView?) {
        listViewReviews?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            listenerTVShowReviewClicked.ontvshowReviewItemListClicked(listTVShowReviews[position].tvShow!!)
        }
    }

    private fun configureTVShowsList(listViewReviews: ListView?) {
        adapterTVShowReview?.notifyDataSetChanged()
        adapterTVShowReview = AdapterTVShowReview(context, listTVShowReviews, IMAGE_BASE_URL)

        listViewReviews?.adapter = adapterTVShowReview

        adapterTVShowReview?.notifyDataSetChanged()
    }

    private fun chechIfTVShowIsAlreadyReviewed(review: UserTVShowReview): Boolean {
        for (tvshow in listTVShowReviews) {
            if (tvshow.tvShow?.id == review.tvShow?.id) {
                return true
            }
        }
        return false
    }


    private fun getUserMovieReviews() {
        val auth = FirebaseAuth.getInstance()
        val currentUserUID = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("ReviewMovie")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userReviewIT = it.getValue(UserMovieReview::class.java)
                        userReviewIT?.let {
                            if (userReviewIT.userUID == currentUserUID) {
                                if (!chechIfFilmIsAlreadyReviewed(userReviewIT))
                                listMovieReviews.add(ModelMovieListReviews(userReviewIT.uidReview, userReviewIT.movie, userReviewIT.review, userReviewIT.userUID))
                            }
                        }
                    }
                    if(listMovieReviews.isEmpty()) {
                        val noDataTextView = activity?.findViewById<TextView>(R.id.noReviewMovieText)
                        noDataTextView?.visibility = View.VISIBLE
                    } else {
                       val listViewReviews = activity?.findViewById<ListView>(R.id.reviewsUserFilm_list)
                        configureMoviesList(listViewReviews)
                        setMoviesListener(listViewReviews)
                    }
                }
                else {
                    val noDataTextView = activity?.findViewById<TextView>(R.id.noReviewMovieText)
                    noDataTextView?.visibility = View.VISIBLE
                }
            }

        })

    }

    private fun setMoviesListener(listViewReviews: ListView?) {
        listViewReviews?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            listenerMovieReviewClicked.onMovieReviewItemListClicked(listMovieReviews[position].movie!!)
        }
    }

    private fun chechIfFilmIsAlreadyReviewed(review:UserMovieReview) :Boolean{
        for (film in listMovieReviews) {
            if (film.movie?.id == review.movie?.id) {
                return true
            }
        }
        return false
    }

    private fun configureMoviesList(listViewReviews: ListView?) {
        adapterMovieReview?.notifyDataSetChanged()
        adapterMovieReview = AdapterMovieReview(context, listMovieReviews, IMAGE_BASE_URL)

        listViewReviews?.adapter = adapterMovieReview

        adapterMovieReview?.notifyDataSetChanged()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listenerMovieReviewClicked = context as OnMovieReviewItemListClicked
        listenerTVShowReviewClicked = context as OnTVShowReviewItemListClicked
    }



}
