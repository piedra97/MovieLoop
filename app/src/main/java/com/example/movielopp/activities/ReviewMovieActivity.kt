package com.example.movielopp.activities


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.movielopp.model.Movie
import kotlinx.android.synthetic.main.activity_review.*
import com.example.movielopp.R
import com.example.movielopp.model.UserMovieReview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class ReviewMovieActivity : AppCompatActivity() {

    private var movieToWork: Movie? = null

    private var reviewConditionsOk = true

    private var userName: String? = null

    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        auth = FirebaseAuth.getInstance()
        movieToWork = intent.getParcelableExtra("movie")
        setUserName(auth!!.currentUser)

    }

    override fun onStart() {
        super.onStart()
        submitButtonActivity.setOnClickListener {
            eraseBreaklines()
            checkMandatoryReviewConditions()
            if (reviewTextActivity.text.isEmpty() || !reviewConditionsOk) {
                Toast.makeText(
                    applicationContext,
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
        val uidReview = UUID.randomUUID().toString()
        val ref = FirebaseDatabase.getInstance().getReference("/ReviewMovie/$uidReview")

        val userReview = UserMovieReview(
            uidReview,
            auth!!.currentUser!!.uid,
            userName!!,
            movieToWork!!,
            reviewTextActivity.text.toString()
        )
        ref.setValue(userReview)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun eraseBreaklines() {
        for (i in reviewTextActivity.text.length downTo 1) {
            if (reviewTextActivity.text.subSequence(i - 1, i).toString() == "\n")
                reviewTextActivity.text.replace(i - 1, i, " ")
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun checkMandatoryReviewConditions() {
        reviewConditionsOk = !reviewTextActivity.text.isEmpty() && reviewTextActivity.text.trim().length > 250
    }
}




