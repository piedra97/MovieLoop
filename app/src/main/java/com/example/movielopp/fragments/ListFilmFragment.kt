package com.example.movielopp.fragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import com.example.movielopp.*
import com.example.movielopp.adapters.AdapterPopularMovies
import com.example.movielopp.interfaces.OnGetMoviesCallBack
import com.example.movielopp.model.Movie
import com.example.movielopp.network.MoviesRepository
import kotlinx.android.synthetic.main.fragment_list_film.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListFilmFragment : Fragment() {

    interface OnMoviesClickedListener {
        fun onMovieClicked(movie:Movie)

    }

    private var adapterCustom: AdapterPopularMovies? = null

    private var moviesRepository: MoviesRepository? = null

    private var sortBy = "POPULAR"

    lateinit var listenerList: OnMoviesClickedListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_film, container, false)

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureList()
        getSortedMovies()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater){
        menu?.clear()
        inflater.inflate(R.menu.menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.sort -> {
                showSortMenu()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        getSortedMovies()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun showSortMenu() {
        val menuItem = activity?.findViewById<View>(R.id.sort)
        val sortMenu = PopupMenu(context, menuItem, Gravity.END)
        sortMenu.inflate(R.menu.menu_movies_sort)
        sortMenu.show()
        sortMenu.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item!!.itemId) {
                    R.id.popular -> {
                        sortBy = MoviesRepository.POPULAR
                        getSortedMovies()
                        return true
                    }
                    R.id.top_rated -> {
                        sortBy = MoviesRepository.TOP_RATED
                        getSortedMovies()
                        return true
                    }
                    R.id.upcoming -> {
                        sortBy = MoviesRepository.UPCOMING
                        getSortedMovies()
                        return true
                    }
                    else -> return false
                }
            }

        })
    }

    private fun getSortedMovies() {
        moviesRepository = MoviesRepository.instance
        moviesRepository!!.getMovies(sortBy, object : OnGetMoviesCallBack {

        override fun onSuccess(movies: List<Movie>) {
            adapterCustom = AdapterPopularMovies(activity?.applicationContext!!, movies) {
                listenerList.onMovieClicked(it)
            }
            movies_listing.adapter = adapterCustom
            progressLayout.visibility = View.GONE
        }

        override fun onError() {
            Toast.makeText(activity, getString(R.string.internet_condition), Toast.LENGTH_SHORT).show()
            progressLayout.visibility = View.GONE
        }
        })
    }


    private fun configureList() {
        movies_listing.setHasFixedSize(true)
        movies_listing.layoutManager = GridLayoutManager(this.context,2)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listenerList = context as OnMoviesClickedListener
    }


}
