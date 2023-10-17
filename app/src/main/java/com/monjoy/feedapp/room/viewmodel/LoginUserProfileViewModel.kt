package com.monjoy.feedapp.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.monjoy.feedapp.network.LoginUserProfileRepository
import com.monjoy.feedapp.network.UserProfileRepository
import com.monjoy.feedapp.room.FeedDatabase
import com.monjoy.feedapp.room.entity.LoginUser
import com.monjoy.feedapp.room.entity.UserProfile
import kotlinx.coroutines.launch

class LoginUserProfileViewModel(application: Application): AndroidViewModel(application) {

    private val loginUserProfileRepository: LoginUserProfileRepository
    private val loginUserProfileList: LiveData<List<LoginUser>>

    init {
        val dbLoginUserProfile = FeedDatabase.getDatabase(application)!!.mLoginUserProfileDao()
        loginUserProfileRepository = LoginUserProfileRepository(dbLoginUserProfile!!)
        loginUserProfileList = loginUserProfileRepository.getUserProfileLive()
    }

    fun insertUserProfile(loginUser: LoginUser){
        viewModelScope.launch {
            loginUserProfileRepository.insert(loginUser)
        }
    }

    fun deleteUserProfile(){
        viewModelScope.launch {
            loginUserProfileRepository.deleteData()
        }
    }

    fun updateUserConfirmation(username:String, isChecked:Boolean){
        viewModelScope.launch {
            loginUserProfileRepository.updateUserConfirmationStatus(username, isChecked)
        }
    }
}