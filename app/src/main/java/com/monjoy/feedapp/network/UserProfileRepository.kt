package com.monjoy.feedapp.network

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.monjoy.feedapp.room.dao.UserProfileDao
import com.monjoy.feedapp.room.entity.UserProfile

class UserProfileRepository(private val userProfileDao: UserProfileDao) {

    private val userProfileList = userProfileDao.getUserProfileLive()

    @WorkerThread
    suspend fun insert(userProfile: UserProfile){
        userProfileDao.insert(userProfile)
    }

    @WorkerThread
    suspend fun deleteData() {
        userProfileDao.deleteAll()
    }

//    @WorkerThread
//    fun updateUserConfirmationStatus(email:String, isConfirmed:Boolean){
//        userProfileDao.updateUserConfirmationStatus(email, isConfirmed)
//    }

//    @WorkerThread
//    fun updateUserProfile(userId:String, accessToken: String, refreshToken: String, secretKey: String) {
//        userProfileDao.updateUserProfile(userId, accessToken, refreshToken, secretKey)
//    }
//
//     @WorkerThread
//    fun updateNewUserPassword(userId:String, password:String) {
//        userProfileDao.updateUserPassword(userId, password)
//    }



    fun getUserProfileLive(): LiveData<List<UserProfile>> {
        return userProfileList
    }

}