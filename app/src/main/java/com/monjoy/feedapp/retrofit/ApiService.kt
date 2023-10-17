package com.monjoy.feedapp.retrofit

import com.monjoy.feedapp.room.entity.PhotoDetail
import retrofit2.http.GET

interface ApiService {

    @GET("photos")
    suspend fun getPhotos(): List<PhotoDetail>

}