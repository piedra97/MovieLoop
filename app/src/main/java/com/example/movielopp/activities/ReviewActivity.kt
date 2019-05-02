package com.example.movielopp.activities


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.movielopp.model.Movie
import kotlinx.android.synthetic.main.activity_review.*
import com.example.movielopp.R
import com.example.movielopp.model.UserMovieRating
import com.example.movielopp.model.UserMovieReview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class ReviewActivity : AppCompatActivity() {


    private var movieToWork: Movie? = null

    private var reviewConditionsOk = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        movieToWork = intent.getParcelableExtra("movie")

    }

    override fun onStart() {
        super.onStart()
        submitButtonActivity.setOnClickListener {
            checkMandatoryReviewConditions()
            if (reviewTextActivity.text.isEmpty() || !reviewConditionsOk) {
                Toast.makeText(applicationContext,
                    "La crítica no a de estar vacía y tiene que tener un mínimo de 5 líneas.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                insertReview()
            }
        }
    }

    private fun insertReview() {
        val auth = FirebaseAuth.getInstance()
        val uidReview = UUID.randomUUID().toString()
        val ref = FirebaseDatabase.getInstance().getReference("/ReviewMovie/$uidReview")

        val userRating = UserMovieReview(uidReview, auth.currentUser!!.uid, movieToWork!!.id.toString(), reviewTextActivity.text.toString(), movieToWork!!.title!!)
        ref.setValue(userRating)

        val intent = Intent(this, MainActivity::class.java)
        Log.d("FireBase", "Review inserted")
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun checkMandatoryReviewConditions() {
        if (reviewTextActivity.lineCount < 5) {
            reviewConditionsOk = false
        }

    }
}




