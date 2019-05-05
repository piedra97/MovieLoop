package com.example.movielopp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.movielopp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {

    interface OnSignOutClicked {
        fun onSignOutClicked()
    }

    interface OnRatingsClicked {
        fun onRatingsClicked()
    }

    interface OnReviewsClicked {
        fun onReviewsClicked()
    }

    interface OnListsClicked {
        fun onListsClicked()
    }

    private lateinit var signOutClickedListener: OnSignOutClicked
    private lateinit var votesClicked: OnRatingsClicked
    private lateinit var listenerReviewsClicked: OnReviewsClicked
    private lateinit var listenerListsClicked: OnListsClicked
    private var userName:String ? = null
    private var userEmail:String ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUserInformation()

        email.text = userEmail

        signOut.setOnClickListener {
            signOutClickedListener.onSignOutClicked()
        }

        userVotes.setOnClickListener {
            votesClicked.onRatingsClicked()
        }

        userReviews.setOnClickListener {
            listenerReviewsClicked.onReviewsClicked()
        }

        userLists.setOnClickListener {
            listenerListsClicked.onListsClicked()
        }
    }


    fun setUserName(user: FirebaseUser?) {
        val database = FirebaseDatabase.getInstance()
        val uidUser = user?.uid
        val ref = database.getReference("users/$uidUser/userName")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userNameDB = dataSnapshot.value
                userName = userNameDB.toString()
                name.text = userName
                profile_container.visibility = View.VISIBLE
            }

        })

    }

    private fun setUserInformation() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val emailUser = user.email
            userEmail = emailUser
        }
        setUserName(user)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        signOutClickedListener = context as OnSignOutClicked
        votesClicked = context as OnRatingsClicked
        listenerReviewsClicked = context as OnReviewsClicked
        listenerListsClicked = context as OnListsClicked
    }




}
