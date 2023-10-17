package com.monjoy.feedapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.monjoy.feedapp.R
import com.monjoy.feedapp.interfaces.OnItemClickListener
import com.monjoy.feedapp.room.entity.PhotoDetail
import com.monjoy.feedapp.utils.PhotoDiffUtils

class ImageAdapter(private val photoList: List<PhotoDetail>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoDetailsItem = photoList[position]

        // Load image into ImageView in the list item
        val imageView = holder.itemView.findViewById<ImageView>(R.id.img_feed_item)
        val tvPhotoId = holder.itemView.findViewById<TextView>(R.id.tv_photo_id)
        tvPhotoId.text = "Photo ID: ${photoDetailsItem.id}"
        // Load image using a library like Glide or Picasso
        Glide.with(holder.itemView.context)
            .load(photoDetailsItem.thumbnailUrl)
            .placeholder(R.drawable.icon_placeholder) // Placeholder image resource
            .error(R.drawable.icon_error_image_load) // Error image resource
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(photoDetailsItem)
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

}
