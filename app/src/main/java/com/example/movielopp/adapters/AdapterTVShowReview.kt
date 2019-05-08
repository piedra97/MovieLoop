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
import com.example.movielopp.model.ModelTVShowListReviews
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class AdapterTVShowReview(var context: Context?, items:ArrayList<ModelTVShowListReviews>, var BASE_URL:String): BaseAdapter(){
    var items:ArrayList<ModelTVShowListReviews>? = null


    init {
        this.items = items

    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder?
        var vista: View? = convertView

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
            load(BASE_URL + items!![position].tvShow?.posterPath).
            placeholder(R.drawable.ic_launcher_foreground).
            into(holder?.image)

        holder?.deleteTextView?.setOnClickListener{
            deleteReview(items!![position].reviewuid)
            items!!.removeAt(position)
            this.notifyDataSetChanged()
        }


        return vista!!
    }

    fun setTVShowListReviews(items: ArrayList<ModelTVShowListReviews>) {
        this.items = items

    }

    private fun deleteReview(uidToDelete:String) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("ReviewTVShow").child(uidToDelete)
        ref.removeValue()
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
        var reviewText: TextView? = null
        var deleteTextView: TextView? = null

        init {
            image = myView.findViewById(R.id.imageMovie)
            reviewText = myView.findViewById(R.id.reviewValue)
            deleteTextView = myView.findViewById(R.id.deleteButton)
        }
    }

}