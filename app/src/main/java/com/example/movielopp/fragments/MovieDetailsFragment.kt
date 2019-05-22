package com.example.movielopp.fragments


import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.CoordinatorLayout
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
import com.example.movielopp.interfaces.*
import com.example.movielopp.model.*
import com.example.movielopp.network.MoviesRepository
import com.example.movielopp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import java.util.UUID.randomUUID


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MovieDetailsFragment : Fragment() {

    interface OnReviewFilmClicked {
        fun onReviewFilmClicked(movie: Movie)
    }

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"
    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"
    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var moviesRepository: MoviesRepository? = null
    private var movieToWork: Movie? = null
    private var userReviews: ArrayList<Review> = ArrayList()
    private lateinit var mDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var spinnerAdapter: ArrayAdapter<Int>
    private lateinit var listenerReview: OnReviewFilmClicked
    private var userHasVoted = false
    private var userHasReviewed = false
    private var userRatingVoted: Int? = null
    private var currentUserRating: UserMovieRating? = null
    private var mContext: Context? = null
    private var check = 0
    private var currentMovieFavoriteToWork: MovieToList? = null
    private var currentMovieWatchedToWork: MovieToList? = null
    private var currentMovieInWatchListToWork: MovieToList? = null
    private var movieIsFav = false
    private var movieIsWatched = false
    private var movieIsWatchList = false
    private var isBackPressedEnabled = false


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

        isBackPressedEnabled = false

        getMovie()

        mDatabase = FirebaseDatabase.getInstance().reference
        if (auth.currentUser != null) {
            val addMovieButton = activity?.findViewById<TextView>(R.id.addButton)
            checkIfUserHasVoted(auth.currentUser!!.uid)
            checkIfUserHasReviewed(auth.currentUser!!.uid)
            reviewButton?.setOnClickListener {
                listenerReview.onReviewFilmClicked(movieToWork!!)
            }
            addMovieButton?.visibility = View.VISIBLE
            addMovieButton?.setOnClickListener {
                showAlertDialog()
            }
        } else {
            getFireBaseReviews()
        }
        handleSpinnerClik()
    }


    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.add_to_list_alert_dialog, null)
        val favoriteButton = mView.findViewById<ToggleButton>(R.id.buttonFavorite)
        val watchedButton = mView.findViewById<ToggleButton>(R.id.buttonWatched)
        val watchListButton = mView.findViewById<ToggleButton>(R.id.buttonWatchList)
        builder.setView(mView)
        val dialog = builder.create()
        favoriteButton.isChecked = movieIsFav
        watchedButton.isChecked = movieIsWatched
        watchListButton.isChecked = movieIsWatchList
        dialog.show()
        setCompoundButtons(favoriteButton, watchedButton, watchListButton)

    }

    fun allowBackPressed() :Boolean {
        return this.isBackPressedEnabled
    }

    private fun setCompoundButtons(favoriteButton:ToggleButton, watchedButton:ToggleButton, watchListButton: ToggleButton) {

        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator


        val randomUID = randomUUID().toString()


        favoriteButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            compoundButton.startAnimation(scaleAnimation)
            if (isChecked) {
                insertMovieInList("FavoriteMovie", randomUID)
            }else {
                deleteMovieFromList("FavoriteMovie", currentMovieFavoriteToWork?.uidList)
                movieIsFav = false
            }

        }

        watchedButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            compoundButton.startAnimation(scaleAnimation)
            if (isChecked) {
                insertMovieInList("WatchedMovieList", randomUID)
            } else {
                deleteMovieFromList("WatchedMovieList", currentMovieWatchedToWork?.uidList)
                movieIsWatched = false
            }
        }

        watchListButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            compoundButton.startAnimation(scaleAnimation)
            if (isChecked) {
                insertMovieInList("WatchListMovie", randomUID)
            } else {
                deleteMovieFromList("WatchListMovie", currentMovieInWatchListToWork?.uidList)
                movieIsWatchList = false
            }

        }
    }

    private fun deleteMovieFromList(listType: String, uidList: String?) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference(listType).child(uidList!!)
        ref.removeValue()
    }

    private fun checkIfMovieIsFav(uid:String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("FavoriteMovie")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userFavoriteMovieIT = it.getValue(MovieToList::class.java)
                        if (userFavoriteMovieIT != null) {
                            if (userFavoriteMovieIT.userUID == uid && userFavoriteMovieIT.movie?.id == movieToWork!!.id) {
                                currentMovieFavoriteToWork = userFavoriteMovieIT
                                movieIsFav = true
                            }
                        }
                    }

                }
            }
        })
    }

    private fun insertMovieInList(listType: String, uidList: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/$listType/$uidList")

         val favoriteMovie = MovieToList(uidList, auth.currentUser!!.uid, movieToWork!!)
        ref.setValue(favoriteMovie)
    }

    private fun getFireBaseReviews() {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("ReviewMovie")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userReviewIT = it.getValue(UserMovieReview::class.java)
                        if (userReviewIT != null && userReviewIT.movie?.id == movieToWork?.id) {
                            val review = Review(userReviewIT.userName, userReviewIT.review)
                            userReviews.add(review)
                            }
                        }
                    }

            }

        })

    }

    private fun checkIfUserHasReviewed(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("ReviewMovie")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userReviewIT = it.getValue(UserMovieReview::class.java)
                        if (userReviewIT != null) {
                            if (userReviewIT.userUID == uid && userReviewIT.movie?.id == movieToWork!!.id) {
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
        val reviewMovieButton = activity?.findViewById<TextView>(R.id.reviewButton)
        if (!userHasReviewed) {
            reviewMovieButton?.visibility = View.VISIBLE
        }

        else {
            reviewMovieButton?.visibility = View.VISIBLE
            reviewMovieButton?.text = "Película criticada"
            reviewMovieButton?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            reviewMovieButton?.isEnabled = false
        }
    }


    private fun handleSpinnerClik() {

        spinnerRating.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinnerRating.error = "Realice su votación."
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                check += 1

                val uidRating = randomUUID().toString()

                val userRatingSelected = spinnerRating.getItemAtPosition(position).toString()

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

    private fun insertRating(uidRating: String, userRatingSelected: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/RatingMovie/$uidRating")

        val userRating = UserMovieRating(uidRating, auth.currentUser!!.uid, movieToWork!!, userRatingSelected)
        ref.setValue(userRating)
    }

    private fun updateRating(uidToUpdate: String?, position:Int) {
        val userRatingToUpdate = spinnerRating.getItemAtPosition(position).toString()
        currentUserRating?.rating = userRatingToUpdate
        val ref = FirebaseDatabase.getInstance().getReference("/RatingMovie/$uidToUpdate")

        ref.setValue(currentUserRating)
    }

    private fun deleteRating(uidToDelete: String?) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("RatingMovie").child(uidToDelete!!)
        ref.removeValue()
    }

    private fun setSpinnerRating() {
        val items = ArrayList<Int> ()
        setItems(items)
        spinnerAdapter = ArrayAdapter(mContext!!, android.R.layout.simple_spinner_dropdown_item, items)
        (spinnerAdapter as ArrayAdapter<*>).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerRating)
        spinner?.adapter = spinnerAdapter
    }

    private fun setUserRatingInteractionsComponents() {
        setSpinnerRating()
        val spinner = activity?.findViewById<Spinner>(R.id.spinnerRating)
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

    private fun checkIfUserHasVoted(currentUserUID: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("RatingMovie")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userRatingIT = it.getValue(UserMovieRating::class.java)
                        if (userRatingIT != null) {
                            if (userRatingIT.userUID == currentUserUID && userRatingIT.movie?.id == movieToWork!!.id && userRatingIT.rating != "Votación") {
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

    private fun setItems(items: ArrayList<Int>) {
        for (i in 1..10) {
            items.add(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()
        movieToWork = arguments!!.getParcelable("movie")
        if (auth.currentUser!= null) {
            checkIfMovieIsFav(auth.currentUser!!.uid)
            checkIfMovieIsWatched(auth.currentUser!!.uid)
            checkIfMovieIsInWatchList(auth.currentUser!!.uid)
        }
    }

    private fun checkIfMovieIsInWatchList(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("WatchListMovie")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userFavoriteMovieIT = it.getValue(MovieToList::class.java)
                        if (userFavoriteMovieIT != null) {
                            if (userFavoriteMovieIT.userUID == uid && userFavoriteMovieIT.movie?.id == movieToWork?.id) {
                                currentMovieInWatchListToWork = userFavoriteMovieIT
                                movieIsWatchList = true
                            }
                        }
                    }

                }
            }
        })
    }

    private fun checkIfMovieIsWatched(uid: String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("WatchedMovieList")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (it in p0.children) {
                        val userFavoriteMovieIT = it.getValue(MovieToList::class.java)
                        if (userFavoriteMovieIT != null) {
                            if (userFavoriteMovieIT.userUID == uid && userFavoriteMovieIT.movie?.id == movieToWork!!.id) {
                                currentMovieWatchedToWork = userFavoriteMovieIT
                                movieIsWatched = true
                            }
                        }
                    }

                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
        if (auth.currentUser != null) {
            hideProfileComponent(menu)


        }else {
            hideSignInComponent(menu)

        }
        hideSortAndSearchComponents(menu)

    }

    private fun hideProfileComponent(menu: Menu?) {
        val profile = menu?.findItem(R.id.profile)
        profile?.isVisible = false
    }

    private fun hideSignInComponent(menu:Menu?) {
        val signIn = menu?.findItem(R.id.signIn)
        signIn?.isVisible = false
    }

    private fun hideSortAndSearchComponents(menu:Menu?) {
        val sort = menu?.findItem(R.id.sort)
        sort?.isVisible = false
        val search = menu?.findItem((R.id.searchItem))
        search?.isVisible = false
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
        listenerReview = context as OnReviewFilmClicked
    }


    private fun getMovie() {
        moviesRepository = MoviesRepository.instance
        moviesRepository?.getMovie(movieToWork!!.id, object : OnGetMovieCallBack {
            override fun onSuccess(movie: Movie) {
                setUIComponents(movie)
                getCredits(movie)
                getTrailers(movie)
                getReviews(movie)
                val fragmentLayout = activity?.findViewById<CoordinatorLayout>(R.id.fragmentDetailsLayout)
                fragmentLayout?.visibility = View.VISIBLE
            }


            override fun onError() {
                showError()
            }

        })
    }

    private fun getCredits(movie: Movie) {
        moviesRepository?.getCredits(movie.id, object : OnGetCreditsCallback {
            override fun onSuccess(cast: List<Cast>, crew: List<Crew>) {
                initializeCrewComponents()
                setCrewComponents(crew)
                initializeCastComponents()
                setCastComponents(cast)
            }

            override fun onError() {
                showError()
            }

        })
    }

    private fun setCastComponents(cast: List<Cast>) {
        val movieCastLayout = activity?.findViewById<LinearLayout>(R.id.movieCast)
        for (castIT in cast) {
            val parent = activity?.layoutInflater?.inflate(R.layout.cast, movieCast, false)
            val card = parent?.findViewById<CardView>(R.id.castCard)
            val profileCast = parent?.findViewById<ImageView>(R.id.castProfile)
            val nameCharacter = parent?.findViewById<TextView>(R.id.characterName)
            val actor = parent?.findViewById<TextView>(R.id.actorName)
            card?.requestLayout()
            if (isAdded) {
                loadProfileCastImage(castIT, profileCast!!)
            }
            nameCharacter?.text = castIT.character
            actor?.text = castIT.name
            movieCastLayout?.addView(parent)
        }
    }

    private fun loadProfileCastImage(castIT: Cast, profile: ImageView) {
        Picasso.get().load(IMAGE_BASE_URL + castIT.profile_path).placeholder(R.drawable.icons8_anonymous_mask_48).into(profile)
    }

    private fun initializeCastComponents() {
        val movieCastText = activity?.findViewById<TextView>(R.id.castLabel)
        val movieCastLayout = activity?.findViewById<LinearLayout>(R.id.movieCast)
        movieCastText?.visibility = View.VISIBLE
        movieCastLayout?.removeAllViews()
    }

    private fun setCrewComponents(crew: List<Crew>) {
        val movieCrew = activity?.findViewById<LinearLayout>(R.id.movieCrewDetails)

        for (crewIT in crew) {
            val parent = activity?.layoutInflater?.inflate(R.layout.crew, movieCrewDetails, false)
            val nameCrew = parent?.findViewById<TextView>(R.id.crewName)
            val jobCrew = parent?.findViewById<TextView>(R.id.crewJob)
            when (crewIT.job) {
                "Director" -> {
                    nameCrew?.text = crewIT.name
                    jobCrew?.text = crewIT.job
                    movieCrew?.addView(parent)
                }
                "Screenplay" -> {
                    nameCrew?.text = crewIT.name
                    jobCrew?.text = "Guión"
                    movieCrew?.addView(parent)
                }
                "Director of Photography" -> {
                    nameCrew?.text = crewIT.name
                    jobCrew?.text = "Director de Fotografía"
                    movieCrew?.addView(parent)
                }
                "Original Music Composer" -> {
                    nameCrew?.text = crewIT.name
                    jobCrew?.text = "Compositor Música Original"
                    movieCrew?.addView(parent)
                }

            }

        }
    }


    private fun initializeCrewComponents() {
        val movieCrewLabel = activity?.findViewById<TextView>(R.id.crewLabel)
        val movieCrew = activity?.findViewById<LinearLayout>(R.id.movieCrewDetails)
        movieCrewLabel?.visibility = View.VISIBLE
        movieCrew?.removeAllViews()
    }

    private fun getReviews(movie: Movie) {
        moviesRepository?.getReviews(movie.id, object: OnGetReviewsCallback {
            override fun onSuccess(reviews: ArrayList<Review>) {
                reviews.addAll(userReviews)
                initializeReviewComponents(reviews)
                setReviewComponents(reviews)
            }

            override fun onError() {
                showError()
            }

        })
    }

    private fun initializeReviewComponents(reviews:List<Review>) {
        val reviewsText = activity?.findViewById<TextView>(R.id.reviewsLabel)
        val movieReviewsLayout = activity?.findViewById<LinearLayout>(R.id.movieReviews)
        if (reviews.isEmpty()) {
            reviewsText?.text = " "
        }
        reviewsText?.visibility = View.VISIBLE
        movieReviewsLayout?.removeAllViews()
    }

    private fun setParsedRevenue(revenue: String) {
        var parsedRevenue = ""
        var count = 0
        for (number in revenue.reversed()) {
            if (count == 3) {
                count = 0
                parsedRevenue += "."
            }
            parsedRevenue += number
            count += 1
        }
        parsedRevenue = parsedRevenue.reversed()

        val movieRevenue = activity?.findViewById<TextView>(R.id.revenueText)
        movieRevenue?.text = "$parsedRevenue $"
    }

    private fun setparsedBudget(budget:String) {
        var parsedBudget = ""
        var count = 0
        for (number in budget.reversed()) {
            if (count == 3) {
                count = 0
                parsedBudget += "."
            }
            parsedBudget += number
            count += 1
        }

        parsedBudget = parsedBudget.reversed()
        parsedBudget.replaceRange(0,1, "")
        val movieBudget = activity?.findViewById<TextView>(R.id.budgetText)
        movieBudget?.text = "$parsedBudget $"
    }

    private fun setUIComponents(movie: Movie) {
        val movieTitle = activity?.findViewById<TextView>(R.id.movieDetailsTitle)
        movieTitle?.text = movie.title
        val summaryText = activity?.findViewById<TextView>(R.id.summaryLabel)
        summaryText?.visibility = View.VISIBLE
        val movieOverview = activity?.findViewById<TextView>(R.id.movieDetailsOverview)
        movieOverview?.text = movie.overview
        val movieRating = activity?.findViewById<TextView>(R.id.rating)
        movieRating?.visibility = View.VISIBLE
        movieRating?.text = movie.rating.toString()
        getGenres(movie)
        val movieReleaseDate = activity?.findViewById<TextView>(R.id.movieDetailsReleaseDate)
        movieReleaseDate?.text = movie.releaseDate
        if (isAdded) {
            loadMovieBackdrop(movie)
        }
        val movieAditionalInformationLabel = activity?.findViewById<TextView>(R.id.aditionalInformationLabel)
        movieAditionalInformationLabel?.visibility = View.VISIBLE
        val movieStatus = activity?.findViewById<TextView>(R.id.statusText)
        movieStatus?.text = movie.status
        val movieOriginalLanguage = activity?.findViewById<TextView>(R.id.loText)
        movieOriginalLanguage?.text = movie.originalLanguage
        val movieOriginalTitle = activity?.findViewById<TextView>(R.id.toText)
        movieOriginalTitle?.text = movie.originalTitle
        val runtime = """${movie.runtime.toString()} min"""
        val movieRunTime = activity?.findViewById<TextView>(R.id.runtimeText)
        movieRunTime?.text = runtime
        val budget = movie.budget.toString()
        setparsedBudget(budget)
        val revenue = movie.revenue.toString()
        setParsedRevenue(revenue)
    }

    private fun loadMovieBackdrop(movie:Movie) {
        val movieBackdrop = activity?.findViewById<ImageView>(R.id.movieDetailsBackdropFragment)
        Picasso.get().
            load(IMAGE_BASE_URL + movie.backdrop).
            into(movieBackdrop)
    }

    private fun setReviewComponents(reviews:List<Review>) {
        val movieReviewsLayout = activity?.findViewById<LinearLayout>(R.id.movieReviews)
        for (review in reviews) {
            val parent = activity?.layoutInflater?.inflate(R.layout.review, movieReviews, false)
            val authorReview = parent?.findViewById<TextView>(R.id.reviewAuthor)
            val contentReview = parent?.findViewById<TextView>(R.id.reviewContent)
            val btnShowMore = parent?.findViewById<TextView>(R.id.showMore)
            authorReview?.text = review.author
            contentReview?.text = review.content
            if(contentReview?.text!!.length > 200) {
                contentReview.maxLines = 3
                btnShowMore?.visibility = View.VISIBLE
            }
            movieReviewsLayout?.addView(parent)
            btnShowMore?.setOnClickListener {

                if (btnShowMore.text.toString() == getString(R.string.show_more)) {
                    contentReview.maxLines = Integer.MAX_VALUE
                    btnShowMore.text = getString(R.string.show_less)
                }
                else {
                    contentReview.maxLines = 3
                    btnShowMore.text = getString(R.string.show_more)
                }
            }
        }
        isBackPressedEnabled = true
    }

    private fun getTrailers(movie: Movie) {
        moviesRepository?.getTrailers(movie.id, object: OnGetTrailersCallback {
            override fun onSuccess(trailers: List<Trailer>) {
                initializeTrailerComponents(trailers)
                setTrailerComponents(trailers)
            }

            override fun onError() {
                showError()
            }

        })
    }

    private fun initializeTrailerComponents(trailers:List<Trailer>) {
        val trailersTextLabel = activity?.findViewById<TextView>(R.id.trailersLabel)
        if (trailers.isEmpty()) {
            trailersTextLabel?.text = ""
        }
        trailersTextLabel?.visibility = View.VISIBLE
        val movieTrailersLayout = activity?.findViewById<LinearLayout>(R.id.movieTrailers)
        movieTrailersLayout?.removeAllViews()
    }

    private fun setTrailerComponents(trailers:List<Trailer>) {
        for (trailer in trailers) {
            val parent = activity?.layoutInflater?.inflate(R.layout.thumbnail_trailer, movieTrailers, false)
            val thumbnail = parent?.findViewById<ImageView>(R.id.thumbnail)
            thumbnail?.requestLayout()
            thumbnail?.setOnClickListener {
                showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.key))
            }
            if (isAdded) {
                loadTrailerPreview(trailer, thumbnail!!)
            }
            val movieTrailersLayout = activity?.findViewById<LinearLayout>(R.id.movieTrailers)
            movieTrailersLayout?.addView(parent)
        }
    }

    private fun loadTrailerPreview(trailer: Trailer, thumbnail:ImageView) {
        Picasso.get().load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.key)).placeholder(R.drawable.ic_launcher).into(thumbnail)
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
                    val movieGenres = activity?.findViewById<TextView>(R.id.genresText)
                    movieGenres?.text = TextUtils.join(", ", currentGenres)
                }
            }

            override fun onError() {
                showError()
            }

        })

    }

    private fun showError() {
        Toast.makeText(activity?.applicationContext, getString(R.string.internet_condition), Toast.LENGTH_SHORT).show()
        isBackPressedEnabled = true
    }

   /* private fun setupToolbar() {
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }

        if((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }*/

    companion object {
        fun newInstance(movie: Movie): MovieDetailsFragment{
            val fragmentMovieDetail = MovieDetailsFragment()
            val args = Bundle()

            args.putParcelable("movie", movie)
            fragmentMovieDetail.arguments = args

            return fragmentMovieDetail
        }
    }




}
