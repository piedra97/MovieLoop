package com.example.movielopp.fragments


import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.example.movielopp.R
import com.example.movielopp.interfaces.*
import com.example.movielopp.model.*
import com.example.movielopp.network.TVShowsRepository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_tvshow_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TVShowDetailsFragment : Fragment() {

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"
    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var tvshowRepository: TVShowsRepository? = null
    private var tvID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        tvID = arguments!!.getInt("IDTVShow")
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //setupToolbar()

        getTvShow()
    }

    private fun getTvShow() {
        tvshowRepository = TVShowsRepository.instance
        tvshowRepository?.getTVShow(tvID, object : OnGetTVShowCallback {
            override fun onSuccess(tvshow: TVShow) {
                setUIComponents(tvshow)
                getCredits(tvshow)
                getVideos(tvshow)
                getReviews(tvshow)
            }


            override fun onError() {
                showError()
            }

        })
    }

    private fun showError() {
        Toast.makeText(context, "Por favor comprueba tu conexión a Internet. GetTVshow", Toast.LENGTH_SHORT).show()
    }

    private fun getReviews(tvshow: TVShow) {
        tvshowRepository?.getReviews(tvshow.id, object: OnGetReviewsCallback {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onSuccess(reviews: ArrayList<Review>) {
                initializeReviewComponents(reviews)
                setReviewComponents(reviews)
            }

            override fun onError() {
                showError()
            }

        })
    }

    private fun setReviewComponents(reviews: List<Review>) {
        for (review in reviews) {
            val parent = layoutInflater.inflate(R.layout.review, TVShowReviews, false)
            val authorReview = parent.findViewById<TextView>(R.id.reviewAuthor)
            val contentReview = parent.findViewById<TextView>(R.id.reviewContent)
            val btnShowMore = parent.findViewById<TextView>(R.id.showMore)
            authorReview.text = review.author
            contentReview.text = review.content
            contentReview.maxLines = 3
            TVShowReviews.addView(parent)
            btnShowMore.setOnClickListener {

                if (btnShowMore.text.toString() == "Muéstrame más...") {
                    contentReview.maxLines = Integer.MAX_VALUE
                    btnShowMore.text = "Muéstrame menos"
                }
                else {
                    contentReview.maxLines = 3
                    btnShowMore.text = "Muéstrame más..."
                }
            }
        }
    }

    private fun initializeReviewComponents(reviews: List<Review>) {
        if (reviews.isEmpty()) {
            tvshowreviewsLabel.text = " "
        }
        tvshowreviewsLabel.visibility = View.VISIBLE
        TVShowReviews.removeAllViews()
    }

    private fun getVideos(tvshow: TVShow) {
        tvshowRepository?.getVideos(tvshow.id, object: OnGetTrailersCallback {
            override fun onSuccess(trailers: List<Trailer>) {
                initializeTrailerComponents(trailers)
                setTrailerComponents(trailers)
            }

            override fun onError() {
                showError()
            }

        })
    }

    private fun setTrailerComponents(trailers: List<Trailer>) {
        for (trailer in trailers) {
            val parent = layoutInflater.inflate(R.layout.thumbnail_trailer, TVShowTrailers, false)
            val thumbnail = parent.findViewById<ImageView>(R.id.thumbnail)
            thumbnail.requestLayout()
            thumbnail.setOnClickListener {
                showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.key))
            }
            loadTrailerPreview(trailer, thumbnail)
            TVShowTrailers.addView(parent)
        }
    }

    private fun loadTrailerPreview(trailer: Trailer, thumbnail: ImageView?) {
        Picasso.get().
            load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.key)).
            placeholder(R.drawable.ic_launcher_foreground).
            into(thumbnail)
    }

    private fun showTrailer(format: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(format))
        startActivity(intent)
    }

    private fun initializeTrailerComponents(trailers: List<Trailer>) {
        if (trailers.isEmpty()) {
            tvShowstrailersLabel.visibility = View.GONE
        }
        tvShowstrailersLabel.visibility = View.VISIBLE
        TVShowTrailers.removeAllViews()
    }


    private fun getCredits(tvshow: TVShow) {
        tvshowRepository?.getCredits(tvshow.id, object : OnGetTVShowCreditsCallback {
            override fun onSuccess(cast: List<Cast>) {
                initializeCastComponents()
                setCastComponents(cast)
            }

            override fun onError() {
                showError()
            }

        })

    }

    private fun setCastComponents(cast: List<Cast>) {
        for (castIT in cast) {
            val parent = layoutInflater.inflate(R.layout.cast, TVShowCast, false)
            val card = parent.findViewById<CardView>(R.id.castCard)
            val profileCast = parent.findViewById<ImageView>(R.id.castProfile)
            val nameCharacter = parent.findViewById<TextView>(R.id.characterName)
            val actor = parent.findViewById<TextView>(R.id.actorName)
            card.requestLayout()
            loadProfileCastImage(castIT, profileCast)
            nameCharacter.text = castIT.character
            actor.text = castIT.name
            TVShowCast.addView(parent)
        }
    }

    private fun loadProfileCastImage(castIT: Cast, profileCast: ImageView?) {
        Picasso.get().
            load(IMAGE_BASE_URL + castIT.profile_path).
            placeholder(R.drawable.ic_launcher_foreground).
            into(profileCast)

    }

    private fun initializeCastComponents() {
        tvshowcastLabel.visibility = View.VISIBLE
        TVShowCast.removeAllViews()
    }

    private fun setUIComponents(tvshow: TVShow) {
        TVShowDetailsTitle.text = tvshow.name
        tvshowsummaryLabel.visibility = View.VISIBLE
        TVShowDetailsOverview.text = tvshow.overview
        tvshowcreatedbyLabel.visibility = View.VISIBLE
        seTvShowCreators(tvshow)
        tvshowrating.visibility = View.VISIBLE
        tvshowrating.text = tvshow.rating.toString()
        setGenres(tvshow)
        TVShowDetailsReleaseDate.text = tvshow.firstAirDate
        loadTVShowBackdrop(tvshow)
        tvshowaditionalInformationLabel.visibility = View.VISIBLE
        tvshowstatusText.text = tvshow.status
        tvshowloText.text = tvshow.originalLanguage
        tvshowtoText.text = tvshow.originalName
        val runtime = """${tvshow.runtime.toString()} min"""
        tvshowruntimeText.text = runtime
        setNetwork(tvshow)
        tvShowSeasonsText.text = tvshow.numberOfSeasons.toString()

    }

    private fun seTvShowCreators(tvshow:TVShow) {
        val currentCreators:ArrayList<String> = ArrayList()
        for (creator in tvshow.createdBy!!) {
            currentCreators.add(creator.name!!)
        }
        TVShowDetailsCreatedBy.text = TextUtils.join(", ", currentCreators)
    }

    private fun setNetwork(tvshow: TVShow) {
        loadNetworkLogo(tvshow)
        networkText.text = tvshow.networks!![0].name
    }

    private fun loadNetworkLogo(tvshow: TVShow) {
        Picasso.get().
            load(IMAGE_BASE_URL + tvshow.networks!![0].logoPath)
            .placeholder(R.drawable.ic_launcher_foreground).
                into(networkLogo)
    }

    private fun loadTVShowBackdrop(tvshow: TVShow) {
        Picasso.get().
            load(IMAGE_BASE_URL + tvshow.backdrop).
            into(tvShowDetailsBackdrop)
    }

    private fun setGenres(tvshow: TVShow) {
        val currentGenres:ArrayList<String> = ArrayList()
        for (genre in tvshow.genres!!) {
            currentGenres.add(genre.name!!)
        }
        TVShowDetailsGenres.text = TextUtils.join(", ", currentGenres)

    }

    companion object {
        fun newInstance(idTVShow: Int): TVShowDetailsFragment{
            val fragmentTVShowDetail = TVShowDetailsFragment()
            val args = Bundle()

            args.putInt("IDTVShow", idTVShow)
            fragmentTVShowDetail.arguments = args

            return fragmentTVShowDetail
        }
    }


}
