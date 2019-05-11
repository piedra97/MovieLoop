package com.example.movielopp.fragments


import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.*
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.*

import com.example.movielopp.R
import com.example.movielopp.interfaces.*
import com.example.movielopp.model.*
import com.example.movielopp.network.TVShowsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_tvshow_details.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TVShowDetailsFragment : Fragment() {

    interface OnReviewTVShowClicked {
        fun onReviewTVShowClicked(tvshow:TVShow)
    }

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"
    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var tvshowRepository: TVShowsRepository? = null
    private var tvShowToWork: TVShow ? = null
    private lateinit var auth:FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private var currentFavoriteTVShow:TVShowToList ? = null
    private var currentTVShowWatchedToWork:TVShowToList ? = null
    private var currentMovieInWatchListToWork: TVShowToList ? = null
    private lateinit var spinnerAdapter: ArrayAdapter<Int>
    private lateinit var listenerReview: OnReviewTVShowClicked
    private var userReviews:ArrayList<Review> = ArrayList()
    private var mContext:Context ? = null
    private var check = 0
    private var tvshowIsFav = false
    private var tvshowIsWatched = false
    private var tvShowIsWatchList = false
    private var userHasVoted = false
    private var userHasReviewed = false
    private var userRatingVoted:Int ? = null
    private var currentUserRating: UserTVShowRating? = null
    private var isBackPressedEnabled = false

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
        auth = FirebaseAuth.getInstance()
        tvShowToWork = arguments!!.getParcelable("TVShow")
        if (auth.currentUser!= null) {
            checkIfTVShowIsFav(auth.currentUser!!.uid)
            checkIfTVShowIsWatched(auth.currentUser!!.uid)
            checkIfTVShowIsInWatchList(auth.currentUser!!.uid)
        }
    }

    private fun checkIfTVShowIsInWatchList(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("WatchListTVShow")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userWatchListTVShowIT = it.getValue(TVShowToList::class.java)
                        if (userWatchListTVShowIT != null) {
                            if (userWatchListTVShowIT.userUID == uid && userWatchListTVShowIT.tvShow?.id == tvShowToWork?.id) {
                                currentMovieInWatchListToWork = userWatchListTVShowIT
                                tvShowIsWatchList = true
                            }
                        }
                    }

                }
            }
        })
    }

    private fun checkIfTVShowIsWatched(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("WatchedTVShowList")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userWatchedTVShowIT = it.getValue(TVShowToList::class.java)
                        if (userWatchedTVShowIT  != null) {
                            if (userWatchedTVShowIT .userUID == uid && userWatchedTVShowIT.tvShow?.id == tvShowToWork!!.id) {
                                currentTVShowWatchedToWork = userWatchedTVShowIT
                                tvshowIsWatched = true
                            }
                        }
                    }

                }
            }
        })

    }

    private fun checkIfTVShowIsFav(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("FavoriteTVShow")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userFavoriteTVShowIT = it.getValue(TVShowToList::class.java)
                        if (userFavoriteTVShowIT != null) {
                            if (userFavoriteTVShowIT.userUID == uid && userFavoriteTVShowIT.tvShow?.id == tvShowToWork!!.id) {
                                currentFavoriteTVShow = userFavoriteTVShowIT
                                tvshowIsFav = true
                            }
                        }
                    }

                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
        if (auth.currentUser != null) {
            hideProfileComponent(menu)


        }else {
            hideSignInComponent(menu)

        }
        hideSortAndSearchComponents(menu)
    }

    private fun hideSortAndSearchComponents(menu: Menu?) {
        val sort = menu?.findItem(R.id.sort)
        sort?.isVisible = false
        val search = menu?.findItem((R.id.searchItem))
        search?.isVisible = false
    }

    private fun hideSignInComponent(menu: Menu?) {
        val signIn = menu?.findItem(R.id.signIn)
        signIn?.isVisible = false
    }

    private fun hideProfileComponent(menu: Menu?) {
        val profile = menu?.findItem(R.id.profile)
        profile?.isVisible = false
    }


    fun allowBackPressed() :Boolean {
        return this.isBackPressedEnabled
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //setupToolbar()

        isBackPressedEnabled = false

        getTvShow()

        mDatabase = FirebaseDatabase.getInstance().reference

        if (auth.currentUser != null) {
            val addMovieButton = activity?.findViewById<TextView>(R.id.addButtonTV)
            checkIfUserHasVoted(auth.currentUser!!.uid)
            checkIfUserHasReviewed(auth.currentUser!!.uid)
            reviewButtonTV?.setOnClickListener {
                listenerReview.onReviewTVShowClicked(tvShowToWork!!)
            }
            addMovieButton?.visibility = View.VISIBLE
            addMovieButton?.setOnClickListener {
                showAlertDialog()
            }
        }
        else {
            getFireBaseReviews()
        }

        handleSpinnerClik()

    }

    private fun handleSpinnerClik() {
        spinnerRatingTV.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinnerRating.error = "Realice su votación."
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                check += 1

                val uidRating = UUID.randomUUID().toString()

                val userRatingSelected = spinnerRatingTV.getItemAtPosition(position).toString()

                if (check > 1 && !userHasVoted && userRatingSelected != "Votación") {

                    insertRating(uidRating, userRatingSelected)


                } else if (check > 1 && userRatingSelected != "Votación"){
                    val uidToUpdate = currentUserRating?.uidRating
                    updateRating(uidToUpdate, position)

                }
                else if (check > 1) {
                    val uidToDelete = currentUserRating?.uidRating
                    deleteRating(uidToDelete)
                }
            }

        }
    }

    private fun deleteRating(uidToDelete: String?) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("RatingTVShow").child(uidToDelete!!)
        ref.removeValue()
    }

    private fun updateRating(uidToUpdate: String?, position: Int) {
        val userRatingToUpdate = spinnerRatingTV.getItemAtPosition(position).toString()
        currentUserRating?.rating = userRatingToUpdate
        val ref = FirebaseDatabase.getInstance().getReference("/RatingTVShow/$uidToUpdate")

        ref.setValue(currentUserRating)
    }

    private fun insertRating(uidRating: String, userRatingSelected: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/RatingTVShow/$uidRating")

        val userRating = UserTVShowRating(uidRating, auth.currentUser!!.uid, tvShowToWork!!, userRatingSelected)
        ref.setValue(userRating)
    }

    private fun getFireBaseReviews() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("ReviewTVShow")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userReviewIT = it.getValue(UserTVShowReview::class.java)
                        if (userReviewIT != null && userReviewIT.tvShow?.id == tvShowToWork?.id) {
                            val review = Review(userReviewIT.userName, userReviewIT.review)
                            userReviews.add(review)
                        }
                    }
                }

            }

        })
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.add_to_list_alert_dialog, null)
        val favoriteButton = mView.findViewById<ToggleButton>(R.id.buttonFavorite)
        val watchedButton = mView.findViewById<ToggleButton>(R.id.buttonWatched)
        val watchListButton = mView.findViewById<ToggleButton>(R.id.buttonWatchList)
        builder.setView(mView)
        val dialog = builder.create()
        favoriteButton.isChecked = tvshowIsFav
        watchedButton.isChecked = tvshowIsWatched
        watchListButton.isChecked = tvShowIsWatchList
        dialog.show()
        setCompoundButtons(favoriteButton, watchedButton, watchListButton)
    }

    private fun setCompoundButtons(favoriteButton: ToggleButton, watchedButton: ToggleButton, watchListButton: ToggleButton) {
        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator


        val randomUID = UUID.randomUUID().toString()


        favoriteButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            compoundButton.startAnimation(scaleAnimation)
            if (isChecked) {
                insertMovieInList("FavoriteTVShow", randomUID)
            }else {
                deleteMovieFromList("FavoriteTVShow", currentFavoriteTVShow?.uidList)
                tvshowIsFav = false
            }

        }

        watchedButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            compoundButton.startAnimation(scaleAnimation)
            if (isChecked) {
                insertMovieInList("WatchedTVShowList", randomUID)
            } else {
                deleteMovieFromList("WatchedTVShowList", currentTVShowWatchedToWork?.uidList)
                tvshowIsWatched = false
            }
        }

        watchListButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            compoundButton.startAnimation(scaleAnimation)
            if (isChecked) {
                insertMovieInList("WatchListTVShow", randomUID)
            } else {
                deleteMovieFromList("WatchListTVShow", currentMovieInWatchListToWork?.uidList)
                tvShowIsWatchList= false
            }

        }
    }

    private fun deleteMovieFromList(listType: String, uidList: String?) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference(listType).child(uidList!!)
        ref.removeValue()
    }

    private fun insertMovieInList(listType: String, uidList: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/$listType/$uidList")

        val favoriteTVShow = TVShowToList(uidList, auth.currentUser!!.uid, tvShowToWork!!)
        ref.setValue(favoriteTVShow)
    }

    private fun checkIfUserHasReviewed(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("ReviewTVShow")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userReviewIT = it.getValue(UserTVShowReview::class.java)
                        if (userReviewIT != null) {
                            if (userReviewIT.userUID == uid && userReviewIT.tvShow?.id == tvShowToWork!!.id) {
                                val review = Review(userReviewIT.userName, userReviewIT.review)
                                userHasReviewed = true
                                userReviews.add(review)
                                setUserReviewInteractionsComponents()
                            }
                        }
                    }

                }

            }

        })

        if (!userHasReviewed) {
            setUserReviewInteractionsComponents()
        }
    }

    private fun setUserReviewInteractionsComponents() {
        val reviewTVShowButton = activity?.findViewById<TextView>(R.id.reviewButtonTV)
        if (!userHasReviewed) {
            reviewTVShowButton?.visibility = View.VISIBLE
        }

        else {
            reviewTVShowButton?.visibility = View.VISIBLE
            reviewTVShowButton?.text = "Serie criticada"
            reviewTVShowButton?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))//resources.getColor(R.color.red, resources.))
            reviewTVShowButton?.isEnabled = false
        }
    }

    private fun checkIfUserHasVoted(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("RatingTVShow")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userRatingIT = it.getValue(UserTVShowRating::class.java)
                        if (userRatingIT != null) {
                            if (userRatingIT.userUID == uid && userRatingIT.tvShow?.id == tvShowToWork!!.id && userRatingIT.rating != "Votación") {
                                userHasVoted = true
                                if (userRatingIT.rating != "") {
                                    userRatingVoted = Integer.parseInt(userRatingIT.rating)
                                }
                                currentUserRating = userRatingIT
                                setUserRatingInteractionsComponents()
                            }
                        }
                    }

                }

            }

        })

        if (!userHasVoted) {
            setUserRatingInteractionsComponents()
        }

    }

    private fun setUserRatingInteractionsComponents() {
        setSpinnerRating()
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerRatingTV)
        if (!userHasVoted) {
            spinner?.visibility = View.VISIBLE
        }

        else {
            val pos: Int
            spinner?.visibility = View.VISIBLE
            if (userRatingVoted == 10) {
                spinner?.setSelection(10)
            }
            else {
                pos = spinnerAdapter.getItem(userRatingVoted!!) as Int
                spinner?.setSelection(pos - 1)
            }
        }
    }

    private fun setSpinnerRating() {
        val items = ArrayList<Int> ()
        setItems(items)
        spinnerAdapter = ArrayAdapter(mContext!!, android.R.layout.simple_spinner_dropdown_item, items)
        (spinnerAdapter as ArrayAdapter<*>).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerRatingTV)
        spinner?.adapter = spinnerAdapter
    }

    private fun setItems(items: ArrayList<Int>) {
        for (i in 1..10) {
            items.add(i)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
        listenerReview = context as OnReviewTVShowClicked
    }


    private fun getTvShow() {
        tvshowRepository = TVShowsRepository.instance
        tvshowRepository?.getTVShow(tvShowToWork?.id!!, object : OnGetTVShowCallback {
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
        Toast.makeText(context, "Por favor comprueba tu conexión a Internet.", Toast.LENGTH_SHORT).show()
        isBackPressedEnabled = true
    }

    private fun getReviews(tvshow: TVShow) {
        tvshowRepository?.getReviews(tvshow.id, object: OnGetReviewsCallback {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onSuccess(reviews: ArrayList<Review>) {
                reviews.addAll(userReviews)
                initializeReviewComponents(reviews)
                setReviewComponents(reviews)
                //isBackPressedEnabled = true
            }

            override fun onError() {
                showError()
            }

        })
    }

    private fun setReviewComponents(reviews: List<Review>) {
        val reviewsLayout = activity?.findViewById<LinearLayout>(R.id.TVShowReviews)
        for (review in reviews) {
            val parent = activity?.layoutInflater?.inflate(R.layout.review, TVShowReviews, false)
            val authorReview = parent?.findViewById<TextView>(R.id.reviewAuthor)
            val contentReview = parent?.findViewById<TextView>(R.id.reviewContent)
            val btnShowMore = parent?.findViewById<TextView>(R.id.showMore)
            authorReview?.text = review.author
            contentReview?.text = review.content
            contentReview?.maxLines = 3
            reviewsLayout?.addView(parent)
            btnShowMore?.setOnClickListener {

                if (btnShowMore.text.toString() == "Muéstrame más...") {
                    contentReview?.maxLines = Integer.MAX_VALUE
                    btnShowMore.text = "Muéstrame menos"
                }
                else {
                    contentReview?.maxLines = 3
                    btnShowMore.text = "Muéstrame más..."
                }
            }
        }
        isBackPressedEnabled = true
    }

    private fun initializeReviewComponents(reviews: List<Review>) {
        val tvShowReviewsLabel = activity?.findViewById<TextView>(R.id.tvshowreviewsLabel)
        if (reviews.isEmpty()) {
            tvShowReviewsLabel?.text = " "
        }
        val tvShowReviewsDetails = activity?.findViewById<LinearLayout>(R.id.TVShowReviews)
        tvShowReviewsLabel?.visibility = View.VISIBLE
        tvShowReviewsDetails?.removeAllViews()
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
        val tvShowTrailerLayout = activity?.findViewById<LinearLayout>(R.id.TVShowTrailers)
        for (trailer in trailers) {
            val parent = activity?.layoutInflater?.inflate(R.layout.thumbnail_trailer, TVShowTrailers, false)
            val thumbnail = parent?.findViewById<ImageView>(R.id.thumbnail)
            thumbnail?.requestLayout()
            thumbnail?.setOnClickListener {
                showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.key))
            }
            if (isAdded) {
                loadTrailerPreview(trailer, thumbnail)
            }
            tvShowTrailerLayout?.addView(parent)
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
        val tvShowTrailersLabel = activity?.findViewById<TextView>(R.id.tvShowstrailersLabel)
        val tvShowTrailersLayout = activity?.findViewById<LinearLayout>(R.id.TVShowTrailers)
        if (trailers.isEmpty()) {
            tvShowTrailersLabel?.text = " "
        }
        tvShowTrailersLabel?.visibility = View.VISIBLE
        tvShowTrailersLayout?.removeAllViews()
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
        val tvShowCastLayout = activity?.findViewById<LinearLayout>(R.id.TVShowCast)
        for (castIT in cast) {
            val parent = activity?.layoutInflater?.inflate(R.layout.cast, TVShowCast, false)
            val card = parent?.findViewById<CardView>(R.id.castCard)
            val profileCast = parent?.findViewById<ImageView>(R.id.castProfile)
            val nameCharacter = parent?.findViewById<TextView>(R.id.characterName)
            val actor = parent?.findViewById<TextView>(R.id.actorName)
            card?.requestLayout()
            if (isAdded) {
                loadProfileCastImage(castIT, profileCast)
            }
            nameCharacter?.text = castIT.character
            actor?.text = castIT.name
            tvShowCastLayout?.addView(parent)
        }
    }

    private fun loadProfileCastImage(castIT: Cast, profileCast: ImageView?) {
        Picasso.get().
            load(IMAGE_BASE_URL + castIT.profile_path).
            placeholder(R.drawable.ic_launcher_foreground).
            into(profileCast)

    }

    private fun initializeCastComponents() {
        val tvShowCastLabel = activity?.findViewById<TextView>(R.id.tvshowcastLabel)
        val tvShowCastLayout = activity?.findViewById<LinearLayout>(R.id.TVShowCast)
        tvShowCastLabel?.visibility = View.VISIBLE
        tvShowCastLayout?.removeAllViews()
    }

    private fun setUIComponents(tvshow: TVShow) {
        val tvShowTitle = activity?.findViewById<TextView>(R.id.TVShowDetailsTitle)
        tvShowTitle?.text = tvshow.name
        val tvShowSummaryLabel = activity?.findViewById<TextView>(R.id.tvshowsummaryLabel)
        tvShowSummaryLabel?.visibility = View.VISIBLE
        val tvShowOverview = activity?.findViewById<TextView>(R.id.TVShowDetailsOverview)
        tvShowOverview?.text = tvshow.overview
        val tvShowCreatedByLabel = activity?.findViewById<TextView>(R.id.tvshowcreatedbyLabel)
        tvShowCreatedByLabel?.visibility = View.VISIBLE
        seTvShowCreators(tvshow)
        val tvShowRating = activity?.findViewById<TextView>(R.id.tvshowrating)
        tvShowRating?.visibility = View.VISIBLE
        tvShowRating?.text = tvshow.rating.toString()
        val tvReleaseDate = activity?.findViewById<TextView>(R.id.TVShowDetailsReleaseDate)
        tvReleaseDate?.text = tvshow.firstAirDate
        if (isAdded) {
            loadTVShowBackdrop(tvshow)
        }
        val tvAditionalLabel = activity?.findViewById<TextView>(R.id.tvshowaditionalInformationLabel)
        tvAditionalLabel?.visibility = View.VISIBLE
        setGenres(tvshow)
        val tvStatus = activity?.findViewById<TextView>(R.id.tvshowstatusText)
        tvStatus?.text = tvshow.status
        val tvLanguage = activity?.findViewById<TextView>(R.id.tvshowloText)
        tvLanguage?.text = tvshow.originalLanguage
        val tvOriginal = activity?.findViewById<TextView>(R.id.tvshowtoText)
        tvOriginal?.text = tvshow.originalName
        val runtime = """${tvshow.runtime.toString()} min"""
        val tvrunTime = activity?.findViewById<TextView>(R.id.tvshowruntimeText)
        tvrunTime?.text = runtime
        setNetwork(tvshow)
        val tvSeasons = activity?.findViewById<TextView>(R.id.tvShowSeasonsText)
        tvSeasons?.text = tvshow.numberOfSeasons.toString()

    }

    private fun seTvShowCreators(tvshow:TVShow) {
        val tvShowCreatedBy = activity?.findViewById<TextView>(R.id.TVShowDetailsCreatedBy)
        val currentCreators:ArrayList<String> = ArrayList()
        for (creator in tvshow.createdBy!!) {
            currentCreators.add(creator.name!!)
        }
        tvShowCreatedBy?.text = TextUtils.join(", ", currentCreators)
    }

    private fun setNetwork(tvshow: TVShow) {
        val tvnetworkText = activity?.findViewById<TextView>(R.id.networkText)
        if (isAdded && !tvshow.networks!!.isEmpty()) {
            loadNetworkLogo(tvshow)
            tvnetworkText?.text = tvshow.networks!![0].name
        }else if(tvshow.networks!!.isEmpty()) {
            tvnetworkText?.text = ""
        }
    }

    private fun loadNetworkLogo(tvshow: TVShow) {
        val tvnetWorkLogo = activity?.findViewById<ImageView>(R.id.networkLogo)
        Picasso.get().load(IMAGE_BASE_URL + tvshow.networks!![0].logoPath)
            .placeholder(R.drawable.ic_launcher_foreground).into(tvnetWorkLogo)
    }

    private fun loadTVShowBackdrop(tvshow: TVShow) {
        val tvBackdrop = activity?.findViewById<ImageView>(R.id.tvShowDetailsBackdrop)
        Picasso.get().
            load(IMAGE_BASE_URL + tvshow.backdrop).
            into(tvBackdrop)
    }

    private fun setGenres(tvshow: TVShow) {
        val tvShowsGenres = activity?.findViewById<TextView>(R.id.tvshowsgenreText)
        val currentGenres:ArrayList<String> = ArrayList()
        for (genre in tvshow.genres!!) {
            currentGenres.add(genre.name!!)
        }
        tvShowsGenres?.text = TextUtils.join(", ", currentGenres)
    }

    companion object {
        fun newInstance(tvShow: TVShow): TVShowDetailsFragment{
            val fragmentTVShowDetail = TVShowDetailsFragment()
            val args = Bundle()

            args.putParcelable("TVShow", tvShow)
            fragmentTVShowDetail.arguments = args

            return fragmentTVShowDetail
        }
    }


}
