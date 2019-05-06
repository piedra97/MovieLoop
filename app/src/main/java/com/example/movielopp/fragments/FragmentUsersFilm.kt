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
import com.example.movielopp.model.Movie
import com.example.movielopp.model.MovieToList
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
class FragmentUsersFilm : Fragment() {

    interface OnMoviesClickedListener {
        fun onMovieClicked(movie: Movie)

    }

    private var listUserFilm:ArrayList<MovieToList> = ArrayList()

    private var adapterMovieInList: AdapterMovieInList? = null

    private var listType: String ? = null

    private lateinit var listenerMovieClicked: OnMoviesClickedListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_film, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getMoviesInList()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapterMovieInList?.setMovies(listUserFilm)
        adapterMovieInList?.notifyDataSetChanged()
    }



    private fun getMoviesInList() {
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
                        val userMovieIT = it.getValue(MovieToList::class.java)
                        userMovieIT?.let {
                            if (userMovieIT.userUID == currentUserUID) {
                                listUserFilm.add(userMovieIT)
                            }
                        }
                    }
                    if(listUserFilm.isEmpty()) {
                        val noDataTextView = activity?.findViewById<TextView>(R.id.noVotesFoundText)
                        noDataTextView?.visibility = View.VISIBLE
                    } else if(context != null){
                        configureList()
                    }
                } else {
                    val noDataTextView = activity?.findViewById<TextView>(R.id.noVotesFoundText)
                    noDataTextView?.visibility = View.VISIBLE
                }
            }

        })


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        listType = arguments!!.getString("listType")

        listenerMovieClicked = context as OnMoviesClickedListener
    }


    private fun configureList() {
        adapterMovieInList = AdapterMovieInList(activity?.applicationContext!!, listUserFilm) {
            listenerMovieClicked.onMovieClicked(it)
        }
        val listMovies = activity?.findViewById<RecyclerView>(R.id.movies_userListing)
        listMovies?.setHasFixedSize(true)
        listMovies?.layoutManager = GridLayoutManager(this.context,2)
        listMovies?.adapter = adapterMovieInList
    }

    companion object {
        fun newInstance(listType: String): FragmentUsersFilm{
            val fragmentUsersList = FragmentUsersFilm()
            val args = Bundle()

            args.putString("listType", listType)
            fragmentUsersList.arguments = args

            return fragmentUsersList
        }
    }


}
