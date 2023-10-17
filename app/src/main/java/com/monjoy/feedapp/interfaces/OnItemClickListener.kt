package com.monjoy.feedapp.interfaces

import com.monjoy.feedapp.room.entity.PhotoDetail

interface OnItemClickListener {

    fun onItemClick(photoDetail: PhotoDetail?) {
    }
}