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
import com.example.movielopp.adapters.AdapterMovieRating
import com.example.movielopp.adapters.AdapterTVShowRating
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
class VotesUserListFragment : Fragment() {

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"

    private var listMovieVotes:ArrayList<ModelMovieListRatings> = ArrayList()

    private var listTVShowVotes:ArrayList<ModelTVShowListRatings> = ArrayList()

    private var adapterMovieRating:AdapterMovieRating? = null

    private var adapterTVShowRating:AdapterTVShowRating? = null

    private lateinit var listenerMovieVote: OnVoteFilmClicked

    private lateinit var listenerTVShowVote: OnVoteTVShowClicked

    interface OnVoteFilmClicked {
        fun onVoteFilmClicked(movie: Movie)
    }

    interface OnVoteTVShowClicked {
        fun onVoteTVShowClicked(tvShow: TVShow)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_votes_user_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserMovieVotes()
        getTVShows()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapterMovieRating?.setMovieListRatings(listMovieVotes)
        adapterMovieRating?.notifyDataSetChanged()
    }

    private fun getTVShows() {
        val auth = FirebaseAuth.getInstance()
        val currentUserUID = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("RatingTVShow")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userRatingIT = it.getValue(UserTVShowRating::class.java)
                        userRatingIT?.let {
                            if (userRatingIT.userUID == currentUserUID) {
                                listTVShowVotes.add(ModelTVShowListRatings(userRatingIT.tvShow, userRatingIT.rating))
                            }
                        }
                    }
                    if(listTVShowVotes.isEmpty()) {
                        val noDataTextView = activity?.findViewById<TextView>(R.id.noRatingTVShowText)
                        noDataTextView?.visibility = View.VISIBLE
                    } else {
                        val listRatings = activity?.findViewById<ListView>(R.id.votesUserTVShow_list)
                        configureListTVShows(listRatings)
                        setListenerTVShows(listRatings)
                    }
                } else {
                    val noDataTextView = activity?.findViewById<TextView>(R.id.noRatingTVShowText)
                    noDataTextView?.visibility = View.VISIBLE
                }
            }

        })


    }

    private fun setListenerTVShows(listRatings: ListView?) {
        listRatings?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            listenerTVShowVote.onVoteTVShowClicked(listTVShowVotes[position].tvShow!!)
        }
    }

    private fun configureListTVShows(listRatings: ListView?) {
        adapterTVShowRating = AdapterTVShowRating(context, listTVShowVotes, IMAGE_BASE_URL)
        listRatings?.adapter = adapterTVShowRating
    }


    private fun getUserMovieVotes() {
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
                                listMovieVotes.add(ModelMovieListRatings(userRatingIT.movie, userRatingIT.rating))
                            }
                        }
                    }
                    if(listMovieVotes.isEmpty()) {
                        val noDataTextView = activity?.findViewById<TextView>(R.id.noRatingMovieText)
                        noDataTextView?.visibility = View.VISIBLE
                    } else {
                        val listRatings = activity?.findViewById<ListView>(R.id.votesUserFilm_list)
                        configureListMovies(listRatings)
                        setListenerMovies(listRatings)
                    }
                } else {
                    val noDataTextView = activity?.findViewById<TextView>(R.id.noRatingMovieText)
                    noDataTextView?.visibility = View.VISIBLE
                }
            }

        })


    }

    private fun setListenerMovies(listRatings: ListView?) {
        listRatings?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            listenerMovieVote.onVoteFilmClicked(listMovieVotes[position].movie!!)
        }
    }


    private fun configureListMovies(listRatings: ListView?) {
        adapterMovieRating = AdapterMovieRating(context, listMovieVotes, IMAGE_BASE_URL)
        listRatings?.adapter = adapterMovieRating
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listenerMovieVote = context as OnVoteFilmClicked
        listenerTVShowVote = context as OnVoteTVShowClicked
    }


}
