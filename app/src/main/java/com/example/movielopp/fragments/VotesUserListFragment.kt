package com.example.movielopp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView

import com.example.movielopp.R
import com.example.movielopp.adapters.AdapterUserRating
import com.example.movielopp.model.ModelListRatings
import com.example.movielopp.model.UserMovieRating
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_reviews_user_list.*
import kotlinx.android.synthetic.main.fragment_votes_user_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class VotesUserListFragment : Fragment() {

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"

    private var listVotes:ArrayList<ModelListRatings> = ArrayList()

    private var adapterRating:AdapterUserRating? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_votes_user_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getTVShows()
        getUserVotes()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapterRating?.setMovieListRatings(listVotes)
        adapterRating?.notifyDataSetChanged()
    }

    private fun getTVShows() {

    }



    private fun getUserVotes() {
        val auth = FirebaseAuth.getInstance()
        val currentUserUID = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("RatingMovie")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userRatingIT = it.getValue(UserMovieRating::class.java)
                        userRatingIT?.let {
                            if (userRatingIT.userUID == currentUserUID) {
                                listVotes.add(ModelListRatings(userRatingIT.posterPath, userRatingIT.rating))
                            }
                        }
                    }
                    if(listVotes.isEmpty()) {
                        val noDataTextView = activity?.findViewById<TextView>(R.id.noVotesFoundText)
                        noDataTextView?.visibility = View.VISIBLE
                    } else {
                        configureList()
                    }
                } else {
                    val noDataTextView = activity?.findViewById<TextView>(R.id.noVotesFoundText)
                    noDataTextView?.visibility = View.VISIBLE
                }
            }

        })


    }


    private fun configureList() {
        adapterRating = AdapterUserRating(context, listVotes, IMAGE_BASE_URL)
        val listRatings = activity?.findViewById<ListView>(R.id.votesUser_list)
        listRatings?.adapter = adapterRating
    }


}
