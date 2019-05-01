package com.example.movielopp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.movielopp.R
import com.example.movielopp.model.Movie
import kotlinx.android.synthetic.main.fragment_review.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ReviewMovieFragment : Fragment() {

    private var reviewConditionsOk = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val button = activity?.findViewById<Button>(R.id.submitButton)
        button?.setOnClickListener {
            checkMandatoryReviewConditions()
            if (!reviewTextFragment.text.isEmpty() && !reviewConditionsOk) {
                Log.d("button", "Button Clicked")
                Toast.makeText(
                    context!!,
                    "La crítica no a de estar vacía y tiene que tener un mínimo de 5 líneas.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun checkMandatoryReviewConditions() {
        reviewTextFragment.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                reviewConditionsOk = reviewTextFragment.text.toString().trim().length < 5
            }
        }
    }


    companion object {
        fun newInstance(movie: Movie): ReviewMovieFragment{
            val fragmentReviewMovie = ReviewMovieFragment()
            val args = Bundle()

            args.putParcelable("movie", movie)
            fragmentReviewMovie.arguments = args

            return fragmentReviewMovie
        }
    }


}
