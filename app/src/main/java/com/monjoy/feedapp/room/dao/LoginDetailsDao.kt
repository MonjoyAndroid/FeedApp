package com.monjoy.feedapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.monjoy.feedapp.room.entity.LoginUser
import com.monjoy.feedapp.room.entity.UserProfile

@Dao
interface LoginDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loginUser: LoginUser)

    @Query("DELETE FROM LoginUserDetails")
    suspend fun deleteAll()

    @Query("UPDATE LoginUserDetails SET IsCheckedRemember = :isChecked WHERE UserName LIKE :username")
    fun updateLoginUserCheckedStatus(username: String, isChecked: Boolean)
    //
    @Query("SELECT * FROM LoginUserDetails")
    fun getLoginUserProfile(): List<LoginUser>

    @Query("SELECT * FROM LoginUserDetails")
    fun getLoginUserProfileLive(): LiveData<List<LoginUser>>
}