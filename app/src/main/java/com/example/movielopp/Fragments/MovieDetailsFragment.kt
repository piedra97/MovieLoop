package com.example.movielopp.Fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.movielopp.Interfaces.OnGetGenresCallback
import com.example.movielopp.Interfaces.OnGetMovieCallBack
import com.example.movielopp.Interfaces.OnGetTrailersCallback
import com.example.movielopp.Model.Genre
import com.example.movielopp.Model.Movie
import com.example.movielopp.Model.Trailer
import com.example.movielopp.Network.MoviesRepository

import com.example.movielopp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MovieDetailsFragment : Fragment() {

    val MOVIE_ID = "movie_id"
    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"
    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var moviesRepository:MoviesRepository? = null
    private var movieID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //setupToolbar()

        getMovie()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieID = arguments!!.getInt("IDMovie")
    }

    private fun getMovie() {
        moviesRepository = MoviesRepository.instance
        moviesRepository?.getMovie(movieID, object : OnGetMovieCallBack {
            override fun onSuccess(movie: Movie) {
                movieDetailsTitle.text = movie.title
                summaryLabel.visibility = View.VISIBLE
                movieDetailsOverview.text = movie.overview
                rating.visibility = View.VISIBLE
                rating.text = movie.rating.toString()
                getGenres(movie)
                movieDetailsReleaseDate.text = movie.releaseDate
                if (!isRemoving) {
                    Picasso.get().
                        load(IMAGE_BASE_URL + movie.backdrop).
                        into(movieDetailsBackdrop)
                }
                getTrailers(movie)
            }


            override fun onError() {
                //activity?.finish()
            }

        })
    }

    private fun getTrailers(movie: Movie) {
        moviesRepository?.getTrailers(movie.id, object: OnGetTrailersCallback {
            override fun onSuccess(trailers: List<Trailer>) {
                if (trailers.isEmpty()) {
                    trailersLabel.text = " "
                }
                trailersLabel.visibility = View.VISIBLE
                movieTrailers.removeAllViews()
                for (trailer in trailers) {
                    val parent = layoutInflater.inflate(R.layout.thumbnail_trailer, movieTrailers, false)
                    val thumbnail = parent.findViewById<ImageView>(R.id.thumbnail)
                    thumbnail.requestLayout()
                    thumbnail.setOnClickListener {
                        showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.key))
                    }
                    Picasso.get().load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.key)).into(thumbnail)
                    movieTrailers.addView(parent)
                }
            }

            override fun onError() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun showTrailer(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun getGenres(movie: Movie) {
        moviesRepository?.getGenres(object : OnGetGenresCallback {
            override fun onSuccess(genres: List<Genre>) {
                if (movie.genres != null) {
                    val currentGenres:ArrayList<String> = ArrayList()
                    for (genre in movie.genres!!) {
                        currentGenres.add(genre.name!!)
                    }
                    movieDetailsGenres.text = TextUtils.join(", ", currentGenres)
                }
            }

            override fun onError() {
                showError()
            }

        })

    }

    private fun showError() {
        Toast.makeText(context, "Por favor comprueba tu conexión a Internet.", Toast.LENGTH_SHORT).show()
    }

    private fun setupToolbar() {
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }

        if((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    companion object {
        fun newInstance(idMovie: Int): MovieDetailsFragment{
            val fragmentMovieDetail = MovieDetailsFragment()
            val args = Bundle()

            args.putInt("IDMovie", idMovie)
            fragmentMovieDetail.arguments = args

            return fragmentMovieDetail
        }
    }




}
