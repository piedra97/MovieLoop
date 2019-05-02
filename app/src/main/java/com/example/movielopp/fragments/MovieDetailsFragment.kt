package com.example.movielopp.fragments


import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.text.TextUtils
import android.view.*
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
        fun onReviewFilmClicked(movie:Movie)
    }

    private val IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780"

    private val YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s"

    private val YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg"
    private var moviesRepository:MoviesRepository? = null
    private var movieToWork:Movie? = null
    private var userReviews:ArrayList<Review> = ArrayList()
    private lateinit var mDatabase:DatabaseReference
    private lateinit var auth:FirebaseAuth
    private lateinit var spinnerAdapter:ArrayAdapter<Int>
    private lateinit var listenerReview:OnReviewFilmClicked
    private var userHasVoted = false
    private var userHasReviewed = false
    private var userRatingVoted:Int ? = null
    private var currentUserRating: UserMovieRating? = null
    private var mContext:Context ? = null
    private var check = 0


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

        mDatabase = FirebaseDatabase.getInstance().reference

        if (auth.currentUser != null) {
            checkIfUserHasVoted(auth.currentUser!!.uid)
            checkIfUserHasReviewed(auth.currentUser!!.uid)
        }
        else {
            getFireBaseReviews()
        }

        handleSpinnerClik()
        reviewButton.setOnClickListener {
            listenerReview.onReviewFilmClicked(movieToWork!!)
        }
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
                        if (userReviewIT != null) {
                            val review = Review(userReviewIT.userName, userReviewIT.review)
                            userReviews.add(review)
                            }
                        }
                    }

                getMovie()

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
                            if (userReviewIT.userUID == uid && userReviewIT.movieID == movieToWork!!.id.toString()) {
                                userHasReviewed = true
                                setUserReviewInteractionsComponents()
                            }
                        }
                    }

                }

                getMovie()

            }

        })

        if (!userHasReviewed) {
            setUserReviewInteractionsComponents()
        }

    }

    private fun setUserReviewInteractionsComponents() {
        if (!userHasReviewed) {
            reviewButton.visibility = View.VISIBLE
        }

        else {
            reviewButton.visibility = View.VISIBLE
            reviewButton.text = "Película criticada"
            reviewButton.isEnabled = false
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

        val userRating = UserMovieRating(uidRating, auth.currentUser!!.uid, movieToWork!!.id.toString(), userRatingSelected, movieToWork!!.posterPath)
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
                            if (userRatingIT.userUID == currentUserUID && userRatingIT.movieID == movieToWork!!.id.toString() && userRatingIT.rating != "Votación") {
                                userHasVoted = true
                                userRatingVoted = Integer.parseInt(userRatingIT.rating)
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
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
        if (auth.currentUser != null) {
            hideProfileComponent(menu)


        }else {
            hidSignInComponent(menu)

        }
        hideSortAndSearchComponents(menu)

    }

    private fun hideProfileComponent(menu: Menu?) {
        val profile = menu?.findItem(R.id.profile)
        profile?.isVisible = false
    }

    private fun hidSignInComponent(menu:Menu?) {
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
        for (castIT in cast) {
            val parent = layoutInflater.inflate(R.layout.cast, movieCast, false)
            val card = parent.findViewById<CardView>(R.id.castCard)
            val profileCast = parent.findViewById<ImageView>(R.id.castProfile)
            val nameCharacter = parent.findViewById<TextView>(R.id.characterName)
            val actor = parent.findViewById<TextView>(R.id.actorName)
            card.requestLayout()
            loadProfileCastImage(castIT, profileCast)
            nameCharacter.text = castIT.character
            actor.text = castIT.name
            movieCast.addView(parent)
        }
    }

    private fun loadProfileCastImage(castIT: Cast, profile: ImageView) {
        Picasso.get().load(IMAGE_BASE_URL + castIT.profile_path).placeholder(R.drawable.ic_launcher_foreground).into(profile)
    }

    private fun initializeCastComponents() {
        castLabel.visibility = View.VISIBLE
        movieCast.removeAllViews()
    }

    private fun setCrewComponents(crew: List<Crew>) {
        for (crewIT in crew) {
            val parent = layoutInflater.inflate(R.layout.crew, movieCrewDetails, false)
            val nameCrew = parent.findViewById<TextView>(R.id.crewName)
            val jobCrew = parent.findViewById<TextView>(R.id.crewJob)
            when (crewIT.job) {
                "Director" -> {
                    nameCrew.text = crewIT.name
                    jobCrew.text = crewIT.job
                    movieCrewDetails.addView(parent)
                }
                "Screenplay" -> {
                    nameCrew.text = crewIT.name
                    jobCrew.text = "Guión"
                    movieCrewDetails.addView(parent)
                }
                "Director of Photography" -> {
                    nameCrew.text = crewIT.name
                    jobCrew.text = "Director de Fotografía"
                    movieCrewDetails.addView(parent)
                }
                "Original Music Composer" -> {
                    nameCrew.text = crewIT.name
                    jobCrew.text = "Compositor Música Original"
                    movieCrewDetails.addView(parent)
                }

            }

        }
    }


    private fun initializeCrewComponents() {
        crewLabel.visibility = View.VISIBLE
        movieCrewDetails.removeAllViews()
    }

    private fun getReviews(movie: Movie) {
        moviesRepository?.getReviews(movie.id, object: OnGetReviewsCallback {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
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
        if (reviews.isEmpty()) {
            reviewsLabel.text = " "
        }
        reviewsLabel.visibility = View.VISIBLE
        movieReviews.removeAllViews()
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

        revenueText.text = "$parsedRevenue $"
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
        budgetText.text = "$parsedBudget $"
    }

    private fun setUIComponents(movie: Movie) {
        movieDetailsTitle.text = movie.title
        summaryLabel.visibility = View.VISIBLE
        movieDetailsOverview.text = movie.overview
        rating.visibility = View.VISIBLE
        rating.text = movie.rating.toString()
        getGenres(movie)
        movieDetailsReleaseDate.text = movie.releaseDate
        loadMovieBackdrop(movie)
        aditionalInformationLabel.visibility = View.VISIBLE
        statusText.text = movie.status
        loText.text = movie.originalLanguage
        toText.text = movie.originalTitle
        val runtime = """${movie.runtime.toString()} min"""
        runtimeText.text = runtime
        val budget = movie.budget.toString()
        setparsedBudget(budget)
        val revenue = movie.revenue.toString()
        setParsedRevenue(revenue)
    }

    private fun loadMovieBackdrop(movie:Movie) {
        Picasso.get().
            load(IMAGE_BASE_URL + movie.backdrop).
            into(movieDetailsBackdrop)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun setReviewComponents(reviews:List<Review>) {
        for (review in reviews) {
            val parent = layoutInflater.inflate(R.layout.review, movieReviews, false)
            val authorReview = parent.findViewById<TextView>(R.id.reviewAuthor)
            val contentReview = parent.findViewById<TextView>(R.id.reviewContent)
            val btnShowMore = parent.findViewById<TextView>(R.id.showMore)
            authorReview.text = review.author
            contentReview.text = review.content
            contentReview.maxLines = 3
            movieReviews.addView(parent)
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
        if (trailers.isEmpty()) {
            trailersLabel.visibility = View.GONE
        }
        trailersLabel.visibility = View.VISIBLE
        movieTrailers.removeAllViews()
    }

    private fun setTrailerComponents(trailers:List<Trailer>) {
        for (trailer in trailers) {
            val parent = layoutInflater.inflate(R.layout.thumbnail_trailer, movieTrailers, false)
            val thumbnail = parent.findViewById<ImageView>(R.id.thumbnail)
            thumbnail.requestLayout()
            thumbnail.setOnClickListener {
                showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.key))
            }
            loadTrailerPreview(trailer, thumbnail)
            movieTrailers.addView(parent)
        }
    }

    private fun loadTrailerPreview(trailer: Trailer, thumbnail:ImageView) {
        Picasso.get().load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.key)).placeholder(R.drawable.ic_launcher_foreground).into(thumbnail)
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
        fun newInstance(movie: Movie): MovieDetailsFragment{
            val fragmentMovieDetail = MovieDetailsFragment()
            val args = Bundle()

            args.putParcelable("movie", movie)
            fragmentMovieDetail.arguments = args

            return fragmentMovieDetail
        }
    }




}
