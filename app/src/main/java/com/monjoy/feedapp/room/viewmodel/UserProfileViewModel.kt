package com.monjoy.feedapp.room.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monjoy.feedapp.network.UserProfileRepository
import com.monjoy.feedapp.room.FeedDatabase
import com.monjoy.feedapp.room.entity.UserProfile
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application): AndroidViewModel(application)  {

    private val userProfileRepository: UserProfileRepository
    private val userProfileList: LiveData<List<UserProfile>>

    init {
        val dbUserProfile = FeedDatabase.getDatabase(application)!!.mUserProfileDao()
        userProfileRepository = UserProfileRepository(dbUserProfile!!)
        userProfileList = userProfileRepository.getUserProfileLive()
    }

    fun insertUserProfile(userProfile: UserProfile){
        viewModelScope.launch {
            userProfileRepository.insert(userProfile)
        }
    }

    fun deleteUserProfile(){
        viewModelScope.launch {
            userProfileRepository.deleteData()
        }
    }

//    fun updateUserConfirmation(email:String, isConfirmed:Boolean){
//        viewModelScope.launch {
//            userProfileRepository.updateUserConfirmationStatus(email, isConfirmed)
//        }
//    }

    fun updateUserProfileData(userId: String, accessToken: String, refreshToken: String, secretKey: String){
        viewModelScope.launch {
//            userProfileRepository.updateUserProfile(userId, accessToken, refreshToken, secretKey)
        }
    }

    fun updateNewPassword(userId: String, encrytedPassword: String){
        viewModelScope.launch {
//            userProfileRepository.updateNewUserPassword(userId, encrytedPassword)
        }
    }

}
