package com.monjoy.feedapp.network

import com.monjoy.feedapp.retrofit.ApiService
import com.monjoy.feedapp.retrofit.model.PhotoAlbum
import com.monjoy.feedapp.room.dao.PhotoDetailDao
import com.monjoy.feedapp.room.entity.PhotoDetail


class PhotoDetailRepository(private val apiService: ApiService, private val photoDetailDao: PhotoDetailDao) {

    suspend fun fetchFeeds() {
        val photos = apiService.getPhotos()
        photoDetailDao.insert(photos)
    }

    suspend fun getFeedsFromDb(): List<PhotoDetail> {
        return photoDetailDao.getFeeds()
    }

    suspend fun updateLikeStatusById(id: Long, isLiked: Boolean) {
        photoDetailDao.updateLikeStatusById(id, isLiked)
    }

}

