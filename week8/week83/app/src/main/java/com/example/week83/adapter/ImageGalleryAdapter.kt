package com.example.week83.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.week83.R
import com.example.week83.model.FavoritesUrl
import com.facebook.drawee.view.SimpleDraweeView

class ImageGalleryAdapter (val imageData: MutableList<FavoritesUrl?>)
    : RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val photoView = inflater.inflate(R.layout.favorite_item, parent, false)
        return MyViewHolder(photoView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageView = holder.draweeView
        val uri: Uri =
            Uri.parse(imageData[position]?.url)
        Log.d("---", imageData[position]?.url.toString())
        imageView.setImageURI(uri)
    }

    override fun getItemCount(): Int {
        return imageData.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var draweeView: ImageView = itemView.findViewById(R.id.iv_favorite_item) as SimpleDraweeView
    }
}