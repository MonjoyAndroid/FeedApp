package com.monjoy.feedapp.network

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.monjoy.feedapp.room.dao.LoginDetailsDao
import com.monjoy.feedapp.room.entity.LoginUser
import com.monjoy.feedapp.room.entity.UserProfile

class LoginUserProfileRepository(private val loginDetailsDao: LoginDetailsDao) {
    private val loginUserProfileList = loginDetailsDao.getLoginUserProfileLive()

    @WorkerThread
    suspend fun insert(loginUser: LoginUser){
        loginDetailsDao.insert(loginUser)
    }

    @WorkerThread
    suspend fun deleteData() {
        loginDetailsDao.deleteAll()
    }

    @WorkerThread
    fun updateUserConfirmationStatus(username:String, isChecked:Boolean){
        loginDetailsDao.updateLoginUserCheckedStatus(username, isChecked)
    }

    fun getUserProfileLive(): LiveData<List<LoginUser>> {
        return loginUserProfileList
    }
}