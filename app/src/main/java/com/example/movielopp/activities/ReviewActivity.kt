package com.example.movielopp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.movielopp.R
import com.example.movielopp.model.Movie
import kotlinx.android.synthetic.main.fragment_review.*

class ReviewActivity : AppCompatActivity() {

    private var movieToWork: Movie? = null

    private var reviewConditionsOk = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        movieToWork = intent.getParcelableExtra("movie")

        checkMandatoryReviewConditions()
        submitButton.bringToFront()
        submitButton.setOnClickListener {
            if (!reviewText.text.isEmpty() && !reviewConditionsOk) {
                Log.d("button", "Button Clicked")
                Toast.makeText(this,
                    "La crítica no a de estar vacía y tiene que tener un mínimo de 5 líneas.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun checkMandatoryReviewConditions() {
        reviewText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                reviewConditionsOk = reviewText.text.toString().trim().length < 5
            }
        }
    }
}




