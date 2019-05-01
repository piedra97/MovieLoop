package com.example.movielopp.activities


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.movielopp.model.Movie
import kotlinx.android.synthetic.main.activity_review.*
import com.example.movielopp.R


class ReviewActivity : AppCompatActivity() /*, View.OnClickListener*/{

    /*override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submitReviewTextView -> {
                if (!reviewText.text.isEmpty() && !reviewConditionsOk) {
                    Log.d("Test", "Button Clicked")
                    Toast.makeText(applicationContext,
                        "La crítica no a de estar vacía y tiene que tener un mínimo de 5 líneas.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
    }*/

    private var movieToWork: Movie? = null

    private var reviewConditionsOk = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        movieToWork = intent.getParcelableExtra("movie")

        checkMandatoryReviewConditions()
         submitReviewTextView.setOnClickListener {
            if (!reviewText.text.isEmpty() && !reviewConditionsOk) {
                Log.d("Test", "Button Clicked")
                Toast.makeText(applicationContext,
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




