package com.monjoy.feedapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.monjoy.feedapp.R
import com.monjoy.feedapp.databinding.ActivitySplashBinding
import com.monjoy.feedapp.room.FeedDatabase
import com.monjoy.feedapp.room.entity.LoginUser
import com.monjoy.feedapp.room.entity.UserProfile
import com.monjoy.feedapp.room.viewmodel.LoginUserProfileViewModel
import com.monjoy.feedapp.room.viewmodel.UserProfileViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var loginUserProfileViewModel: LoginUserProfileViewModel
    private var userProfile: List<UserProfile>? = null
    private var loginProfile: List<LoginUser>? = null
    private lateinit var db: FeedDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Animate the loading of new activity
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        binding.imgApp.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise))
        userProfileViewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        loginUserProfileViewModel = ViewModelProvider(this)[LoginUserProfileViewModel::class.java]
        db = FeedDatabase.getDatabase(this)!!
        fetchDataFromDatabase()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            checkingForNextScreen()
            // Close this activity
            finish()

        }, 3000)
    }

    private fun fetchDataFromDatabase(){
        userProfile = db.mUserProfileDao()!!.getUserProfile()
        loginProfile = db.mLoginUserProfileDao()!!.getLoginUserProfile()
    }

    private fun checkingForNextScreen() {
        if(userProfile.isNullOrEmpty()){
            callSignUpScreen()
        }else{
            if(loginProfile.isNullOrEmpty()){
                callLoginScreen()
            }else{
                if(loginProfile!![0].isCheckedRemember){
                    callHomeScreen()
                }else{
                    callLoginScreen()
                }

            }

        }
        finishAffinity()
    }

    private fun callSignUpScreen(){
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun callLoginScreen(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun callHomeScreen(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}