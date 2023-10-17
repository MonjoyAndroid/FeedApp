package com.monjoy.feedapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.monjoy.feedapp.room.entity.PhotoDetail

@Dao
interface PhotoDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photoList: List<PhotoDetail>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFeed(photoDetail :PhotoDetail)

    @Delete
    suspend fun deleteFeed(photoDetail :PhotoDetail)

    @Query("SELECT * FROM PhotoDetail")
    suspend fun getFeeds(): List<PhotoDetail>

    @Query("Select * from PhotoDetail")
    fun getAllFeeds(): LiveData<List<PhotoDetail>>

    @Update
    suspend fun updateFeed(photoDetail :PhotoDetail)

    @Query("UPDATE PhotoDetail SET IsLiked = :isLiked WHERE id = :id")
    suspend fun updateLikeStatusById(id: Long, isLiked: Boolean)
}