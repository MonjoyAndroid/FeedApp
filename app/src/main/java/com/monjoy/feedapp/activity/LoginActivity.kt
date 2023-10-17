package com.monjoy.feedapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.lifecycle.ViewModelProvider
import com.monjoy.feedapp.R
import com.monjoy.feedapp.databinding.ActivityLoginBinding
import com.monjoy.feedapp.room.FeedDatabase
import com.monjoy.feedapp.room.entity.LoginUser
import com.monjoy.feedapp.room.entity.UserProfile
import com.monjoy.feedapp.room.viewmodel.LoginUserProfileViewModel
import com.monjoy.feedapp.room.viewmodel.UserProfileViewModel
import com.monjoy.feedapp.utils.CommonUtils

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var loginUserProfileViewModel: LoginUserProfileViewModel
    private var userProfileLst: List<UserProfile>? = null
    private var loginProfileLst: List<LoginUser>? = null
    private lateinit var db: FeedDatabase
    private var userExist: Boolean = false
    private var isUserChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userProfileViewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        loginUserProfileViewModel = ViewModelProvider(this)[LoginUserProfileViewModel::class.java]
        db = FeedDatabase.getDatabase(this)!!
        fetchDataFromDatabase()
        setCheckedChangedListener()


        binding.imgShowHidePass.setOnClickListener {
            if (binding.etPassword.length() > 0)
                showHidePass()
        }

        binding.tvSignup.setOnClickListener {
            openSignUpScreen()
        }

        binding.buttonLogin.setOnClickListener{
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            validateCredentials(username, password)
        }
    }

    private fun setCheckedChangedListener() {
        binding.checkBoxRemember.setOnCheckedChangeListener { buttonView, isChecked ->
            isUserChecked = isChecked
        }

    }

    private fun validateCredentials(username: String, password: String) {
        if (username.isEmpty() or password.isEmpty()) {
            CommonUtils.alertDialog(this, resources.getString(R.string.username_pass_mandatory))
            return
        }

        for (i in 0 until userProfileLst!!.size){
            if(userProfileLst!![i].userName == username){
                userExist = true
                if(userProfileLst!![i].password == password){
                    loggedInUser(userProfileLst!![i].id!!, username, password, isUserChecked)
                }else{
                    CommonUtils.alertDialog(this, resources.getString(R.string.wrong_password))
                }
                return
            }

        }

        if(!userExist){
            CommonUtils.alertDialog(this, resources.getString(R.string.user_not_exist))
        }
    }

    private fun loggedInUser(userId: Long, username: String, pass: String, isChecked: Boolean) {
        val loginUser = LoginUser(userId,username, pass, isChecked)
        loginUserProfileViewModel.deleteUserProfile()
        val job = loginUserProfileViewModel.insertUserProfile(loginUser)
        callHomeScreen()
    }

    @SuppressLint("SetTextI18n")
    fun showHidePass() {
        if(binding.imgShowHidePass.contentDescription == "Show"){
            //Show Password
            binding.imgShowHidePass.contentDescription = "Hide"
            binding.imgShowHidePass.setBackgroundResource(R.drawable.icon_show)
            binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }else{
            //Hide Password
            binding.imgShowHidePass.contentDescription = "Show"
            binding.imgShowHidePass.setBackgroundResource(R.drawable.icon_hide)
            binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.etPassword.setSelection(binding.etPassword.length())
    }

    private fun fetchDataFromDatabase(){
        userProfileLst = db.mUserProfileDao()!!.getUserProfile()
        loginProfileLst = db.mLoginUserProfileDao()!!.getLoginUserProfile()
    }

    private fun openSignUpScreen() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun callHomeScreen(){
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finishAffinity()
    }
}