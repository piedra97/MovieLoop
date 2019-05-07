package com.example.movielopp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.movielopp.R
import com.example.movielopp.model.ModelTVShowListRatings
import com.squareup.picasso.Picasso

class AdapterTVShowRating(var context: Context?, items:ArrayList<ModelTVShowListRatings>, var BASE_URL:String): BaseAdapter(){
    var items:ArrayList<ModelTVShowListRatings>? = null

    init {
        this.items = items

    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder?
        var vista: View? = convertView

        if(vista == null){

            vista = LayoutInflater.from(context).inflate(R.layout.user_votes_template,null)
            holder = ViewHolder(vista)
            vista.tag = holder

        }else {
            holder = vista.tag as? ViewHolder
        }


        holder?.ratingText?.text = items!![position].ratingValue
        Picasso.
            get().
            load(BASE_URL + items!![position].tvShow?.posterPath).
            placeholder(R.drawable.ic_launcher_foreground).
            into(holder?.image)


        return vista!!
    }

    fun setMovieListRatings(items: ArrayList<ModelTVShowListRatings>) {
        this.items = items

    }

    override fun getItem(position: Int): Any {
        return items?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items?.count()!!

    }

    private class ViewHolder(myView: View){
        var image: ImageView? = null
        var ratingText: TextView? = null

        init {
            image = myView.findViewById(R.id.item_movie_poster)
            ratingText = myView.findViewById(R.id.item_movie_rating)
        }
    }

}