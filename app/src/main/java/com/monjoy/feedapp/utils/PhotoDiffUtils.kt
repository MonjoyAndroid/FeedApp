package com.monjoy.feedapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.monjoy.feedapp.room.entity.PhotoDetail

class PhotoDiffUtils : DiffUtil.ItemCallback<PhotoDetail>()  {
    override fun areItemsTheSame(oldItem: PhotoDetail, newItem: PhotoDetail): Boolean {
        return oldItem.id == newItem.id // Compare based on unique identifier
    }

    override fun areContentsTheSame(oldItem: PhotoDetail, newItem: PhotoDetail): Boolean {
        return oldItem == newItem // Compare content for equality
    }
}