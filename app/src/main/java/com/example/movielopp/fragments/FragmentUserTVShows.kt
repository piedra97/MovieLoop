package com.example.movielopp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.movielopp.R
import com.example.movielopp.adapters.AdapterMovieInList
import com.example.movielopp.adapters.AdapterTVShowInList
import com.example.movielopp.model.Movie
import com.example.movielopp.model.MovieToList
import com.example.movielopp.model.TVShow
import com.example.movielopp.model.TVShowToList
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
class FragmentUsersTVShows : Fragment() {

    interface OnTVshowClickedListener {
        fun onTVShowClicked(tvShow: TVShow)
    }

    private var listUserTVShow:ArrayList<TVShowToList> = ArrayList()

    private var adapterTVShowInList: AdapterTVShowInList? = null

    private var listType: String ? = null

    private lateinit var listenerTVShowClicked: OnTVshowClickedListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_tvshows, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getTVShowsInList()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapterTVShowInList?.setTVShows(listUserTVShow)
        adapterTVShowInList?.notifyDataSetChanged()
    }



    private fun getTVShowsInList() {
        val auth = FirebaseAuth.getInstance()
        val currentUserUID = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference(listType!!)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userMovieIT = it.getValue(TVShowToList::class.java)
                        userMovieIT?.let {
                            if (userMovieIT.userUID == currentUserUID) {
                                listUserTVShow.add(userMovieIT)
                            }
                        }
                    }
                    if(listUserTVShow.isEmpty()) {
                        val noDataTextView = activity?.findViewById<TextView>(R.id.noTVShowsFoundText)
                        noDataTextView?.visibility = View.VISIBLE
                    } else if(context != null){
                        configureList()
                    }
                } else {
                    val noDataTextView = activity?.findViewById<TextView>(R.id.noTVShowsFoundText)
                    noDataTextView?.visibility = View.VISIBLE
                }
            }

        })


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listType = arguments!!.getString("listType")

        listenerTVShowClicked = context as OnTVshowClickedListener
    }


    private fun configureList() {
        adapterTVShowInList = AdapterTVShowInList(activity?.applicationContext!!, listUserTVShow) {
            listenerTVShowClicked.onTVShowClicked(it)
        }
        val listMovies = activity?.findViewById<RecyclerView>(R.id.tvShows_userListing)
        listMovies?.setHasFixedSize(true)
        listMovies?.layoutManager = GridLayoutManager(this.context,2)
        listMovies?.adapter = adapterTVShowInList
    }

    companion object {
        fun newInstance(listType: String): FragmentUsersTVShows{
            val fragmentUsersList = FragmentUsersTVShows()
            val args = Bundle()

            args.putString("listType", listType)
            fragmentUsersList.arguments = args

            return fragmentUsersList
        }
    }


}
