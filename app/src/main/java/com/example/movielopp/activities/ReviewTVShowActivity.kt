package com.example.movielopp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.movielopp.R
import com.example.movielopp.model.TVShow
import com.example.movielopp.model.UserTVShowReview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_review_tvshow.*
import java.util.*

class ReviewTVShowActivity : AppCompatActivity() {

    private var tvShowToWork: TVShow? = null

    private var reviewConditionsOk = true

    private var userName:String? = null

    private var auth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_tvshow)

        auth = FirebaseAuth.getInstance()
        tvShowToWork = intent.getParcelableExtra("tvShow")
        setUserName(auth!!.currentUser)

    }

    override fun onStart() {
        super.onStart()
        submitButtonTVShowActivity.setOnClickListener {
            checkMandatoryReviewConditions()
            if (reviewTextTVShowActivity.text.isEmpty() || !reviewConditionsOk) {
                Toast.makeText(applicationContext,
                    "La crítica no ha de estar vacía y tiene que tener un mínimo de 250 caracteres.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                insertReview()
            }
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

            }

        })

    }

    private fun insertReview() {

        eraseBreaklines()
        val uidReview = UUID.randomUUID().toString()
        val ref = FirebaseDatabase.getInstance().getReference("/ReviewTVShow/$uidReview")

        val userReview = UserTVShowReview(uidReview, auth!!.currentUser!!.uid, userName!!, tvShowToWork!!, reviewTextTVShowActivity.text.toString())
        ref.setValue(userReview)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun eraseBreaklines() {
        for (i in reviewTextTVShowActivity.text.length downTo 1) {
            if (reviewTextTVShowActivity.text.subSequence(i - 1, i).toString() == "\n")
                reviewTextTVShowActivity.text.replace(i - 1, i, " ")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun checkMandatoryReviewConditions() {
        reviewConditionsOk = reviewTextTVShowActivity.lineCount >= 5 && !reviewTextTVShowActivity.text.isEmpty()
    }
}
