package com.example.movielopp.Fragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import com.example.movielopp.*
import com.example.movielopp.Adapters.AdapterPopularMovies
import com.example.movielopp.Interfaces.OnGetMoviesCallBack
import com.example.movielopp.Model.Movie
import com.example.movielopp.Network.MoviesRepository
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
        fun onMovieClicked(iDMovie:Int)

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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list_film, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureList()
        getSortedMovies()
        //setToolbar()
    }

    private fun setToolbar() {
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater){
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun showSortMenu() {
        val sortMenu = PopupMenu(context, view, Gravity.END)
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
        sortMenu.inflate(R.menu.menu_movies_sort)
        sortMenu.show()
    }

    private fun getSortedMovies() {
        moviesRepository = MoviesRepository.instance
        moviesRepository!!.getMovies(sortBy, object : OnGetMoviesCallBack {

        override fun onSuccess(movies: List<Movie>) {
            adapterCustom = AdapterPopularMovies(context!!, movies) {
                listenerList.onMovieClicked(it)
            }
            movies_listing.adapter = adapterCustom
            //adapterCustom!!.setMovies(movies)
            //adapterCustom!!.notifyDataSetChanged()
            listFilms_progressBar.visibility = View.GONE
            //setTitle()
        }

        override fun onError() {
            Toast.makeText(activity, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
        }
        })
    }

    private fun setTitle() {
        when(sortBy) {
            MoviesRepository.POPULAR -> {
                (activity as AppCompatActivity).supportActionBar?.title = "Popular"
            }
            MoviesRepository.TOP_RATED -> {
                (activity as AppCompatActivity).supportActionBar?.title = "Mejor Valoradas"
            }
            MoviesRepository.UPCOMING -> {
                (activity as AppCompatActivity).supportActionBar?.title = "Próximamente"
            }
        }
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
