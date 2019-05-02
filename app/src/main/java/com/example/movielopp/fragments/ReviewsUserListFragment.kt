package com.example.movielopp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.example.movielopp.R
import com.example.movielopp.adapters.AdapterUserReview
import com.example.movielopp.model.ModelListReviews
import com.example.movielopp.model.UserMovieReview
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



    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"

    private var listReviews:ArrayList<ModelListReviews> = ArrayList()

    private var adapterReview: AdapterUserReview? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews_user_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getTVShows()
        getUserReviews()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapterReview?.setMovieListReviews(listReviews)
        adapterReview?.notifyDataSetChanged()
    }

    private fun getTVShows() {

    }



    private fun getUserReviews() {
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
                                listReviews.add(ModelListReviews(userReviewIT.posterPath, userReviewIT.review, userReviewIT.userUID))
                            }
                        }
                    }
                    configureList()
                }

            }

        })

    }


    private fun configureList() {
        adapterReview = AdapterUserReview(context, listReviews, IMAGE_BASE_URL)
        val listRatings = activity?.findViewById<ListView>(R.id.reviewsUser_list)
        listRatings?.adapter = adapterReview
    }



}
