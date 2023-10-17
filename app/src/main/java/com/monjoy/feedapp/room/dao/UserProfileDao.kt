package com.monjoy.feedapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.monjoy.feedapp.room.entity.UserProfile

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userProfile: UserProfile)

    @Query("DELETE FROM UserProfileDetails")
    suspend fun deleteAll()

//    @Query("UPDATE UserProfileDetails SET AccessToken = :accessToken ,RefreshToken= :refreshToken,SecretKey= :secretKey WHERE UserId LIKE :userId ")
//    fun updateUserProfile(userId: String, accessToken: String, refreshToken: String, secretKey: String?)
//
// @Query("UPDATE UserProfileDetails SET UserPassword = :newPassword WHERE UserId LIKE :userId ")
//    fun updateUserPassword(userId: String,newPassword:String)

//    @Query("UPDATE UserProfileDetails SET IsCheckedRemember = :isChecked WHERE UserName LIKE :username")
//    fun updateUserConfirmationStatus(username: String, isChecked: Boolean)
//
    @Query("SELECT * FROM UserProfileDetails")
    fun getUserProfile(): List<UserProfile>

    @Query("SELECT * FROM UserProfileDetails")
    fun getUserProfileLive(): LiveData<List<UserProfile>>
}