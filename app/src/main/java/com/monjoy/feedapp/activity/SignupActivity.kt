package com.monjoy.feedapp.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.monjoy.feedapp.R
import com.monjoy.feedapp.databinding.ActivityLoginBinding
import com.monjoy.feedapp.databinding.ActivitySignupBinding
import com.monjoy.feedapp.room.FeedDatabase
import com.monjoy.feedapp.room.entity.LoginUser
import com.monjoy.feedapp.room.entity.UserProfile
import com.monjoy.feedapp.room.viewmodel.LoginUserProfileViewModel
import com.monjoy.feedapp.room.viewmodel.UserProfileViewModel
import com.monjoy.feedapp.utils.CommonUtils
import com.monjoy.feedapp.utils.NetworkUtils

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private var showInfoPop: PopupWindow?= null
    private lateinit var userProfileViewModel: UserProfileViewModel
    private var userProfileLst: List<UserProfile>? = null
    private lateinit var db: FeedDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userProfileViewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        db = FeedDatabase.getDatabase(this)!!
        fetchDataFromDatabase()

        binding.imgInfoPass.setOnClickListener {
            showInfoPop = showInfoPopup()
            showInfoPop?.isOutsideTouchable = true
            showInfoPop?.isFocusable = false
//            showInfoPop?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            showInfoPop?.showAsDropDown(binding.imgInfoPass, -760, 10)
        }

        binding.imgShowHidePass.setOnClickListener {
            if (binding.etPassword.length() > 0)
                showHidePass("ShowPass")
        }

        binding.imgShowHideConfirmPass.setOnClickListener {
            if (binding.etConfirmPassword.length() > 0)
                showHidePass("ShowConfirmPass")
        }

        binding.buttonSignup.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            checkCredentialsValidation(username, password, confirmPassword)
        }
    }

    private fun fetchDataFromDatabase(){
        userProfileLst = db.mUserProfileDao()!!.getUserProfile()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkCredentialsValidation(username: String, password: String, confirmPassword: String) {
        if(username.isEmpty() or password.isEmpty() or confirmPassword.isEmpty()){
            if(username.isEmpty() and password.isEmpty() and confirmPassword.isEmpty()){
                binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_red)
                binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
                binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
            }else{
                if(username.isEmpty() and password.isEmpty()){
                    binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_red)
                    binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
                    binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
                }else if(username.isEmpty() and confirmPassword.isEmpty()){
                    binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_red)
                    binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
                    binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
                }else if (password.isEmpty() and confirmPassword.isEmpty()){
                    binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_grey)
                    binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
                    binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
                }else{
                    if (username.isEmpty()){
                        binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_red)
                        binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
                        binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
                    }else if(password.isEmpty()){
                        binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_grey)
                        binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
                        binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
                    }else if(confirmPassword.isEmpty()){
                        binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_grey)
                        binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
                        binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
                    }
                }
            }
            return
        }else if(!CommonUtils.isPasswordValidate(password)){
            binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_grey)
            binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
            binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
            CommonUtils.alertDialog(this, resources.getString(R.string.password_not_valid))
            return
        }else if(password != confirmPassword){
            binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_grey)
            binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
            binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_red)
            CommonUtils.alertDialog(this, resources.getString(R.string.password_confirm_password_not_matching))
            return
        }else{
            binding.etUsername.background = resources.getDrawable(R.drawable.bg_border_grey)
            binding.rlPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
            binding.rlConfirmPasswordBox.background = resources.getDrawable(R.drawable.bg_border_grey)
            registerUser(username, password)
        }
    }


    private fun showInfoPopup(): PopupWindow {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.info_message_layout, null)
        view.findViewById<TextView>(R.id.tv_message).text = resources.getString(R.string.pass_suggestion_msg)
        return PopupWindow(view, 800, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun showHidePass(showHideFor: String) {
        when(showHideFor) {
            "ShowPass" -> {
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
            "ShowConfirmPass" -> {
                if (binding.imgShowHideConfirmPass.contentDescription == "Show") {
                    binding.imgShowHideConfirmPass.contentDescription = "Hide"
                    binding.imgShowHideConfirmPass.setBackgroundResource(R.drawable.icon_show)
                    binding.etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    binding.imgShowHideConfirmPass.contentDescription = "Show"
                    binding.imgShowHideConfirmPass.setBackgroundResource(R.drawable.icon_hide)
                    binding.etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                binding.etConfirmPassword.setSelection(binding.etConfirmPassword.length())
            }
        }
    }

    private fun registerUser(username: String, password: String) {
        val userProfile = UserProfile(null,username, password)
        for (i in 0 until userProfileLst!!.size){
            if(userProfileLst!![i].userName == userProfile.userName){
                CommonUtils.showToastMessage(this, resources.getString(R.string.user_already_registered))
                return
            }

        }
        val job = userProfileViewModel.insertUserProfile(userProfile)
        callLoginScreen()
        CommonUtils.showToastMessage(this, resources.getString(R.string.user_registered))

    }

    private fun callLoginScreen(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finishAffinity()
    }
}