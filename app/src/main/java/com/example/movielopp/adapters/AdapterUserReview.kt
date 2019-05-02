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
import com.example.movielopp.model.ModelListReviews
import com.squareup.picasso.Picasso

class AdapterUserReview(var context: Context?, items:ArrayList<ModelListReviews>, var BASE_URL:String): BaseAdapter(){
    var items:ArrayList<ModelListReviews>? = null

    init {
        this.items = items

    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder?
        var vista:View? = convertView

        if(vista == null){

            vista = LayoutInflater.from(context).inflate(R.layout.template_user_review,null)
            holder = ViewHolder(vista)
            vista.tag = holder

        }else {
            holder = vista.tag as? ViewHolder
        }


        holder?.reviewText?.text = items!![position].reviewValue
        holder?.reviewText?.maxLines = 3
        Picasso.
            get().
            load(BASE_URL + items!![position].movieURL).
            placeholder(R.drawable.ic_launcher_foreground).
            into(holder?.image)

        holder?.deleteTextView?.setOnClickListener{
            items!!.removeAt(position)
            this.notifyDataSetChanged()
        }


        return vista!!
    }

    fun setMovieListReviews(items: ArrayList<ModelListReviews>) {
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

    private class ViewHolder(myView:View){
        var image: ImageView? = null
        var reviewText: TextView? = null
        var deleteTextView: TextView? = null

        init {
            image = myView.findViewById(R.id.imageMovie)
            reviewText = myView.findViewById(R.id.reviewValue)
            deleteTextView = myView.findViewById(R.id.deleteButton)
        }
    }

}