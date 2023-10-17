package com.monjoy.feedapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.monjoy.feedapp.R
import com.monjoy.feedapp.adapter.ImageAdapter
import com.monjoy.feedapp.databinding.ActivityHomeBinding
import com.monjoy.feedapp.interfaces.OnItemClickListener
import com.monjoy.feedapp.network.PhotoDetailRepository
import com.monjoy.feedapp.retrofit.ApiService
import com.monjoy.feedapp.retrofit.RetrofitClient
import com.monjoy.feedapp.room.FeedDatabase
import com.monjoy.feedapp.room.entity.PhotoDetail
import com.monjoy.feedapp.room.viewmodel.LoginUserProfileViewModel
import com.monjoy.feedapp.room.viewmodel.PhotoDetailViewModel
import com.monjoy.feedapp.room.viewmodel.PhotoDetailViewModelFactory
import com.monjoy.feedapp.utils.CommonUtils
import com.monjoy.feedapp.utils.NetworkUtils
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding:ActivityHomeBinding
    private lateinit var loginUserProfileViewModel: LoginUserProfileViewModel
    private lateinit var photoDetailViewModel: PhotoDetailViewModel
    private lateinit var apiService: ApiService
    private lateinit var onItemClickListener: OnItemClickListener
    private var imageAdapter: ImageAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = FeedDatabase.getDatabase(this)!!
        apiService = RetrofitClient.createService(ApiService::class.java)
        val repository = PhotoDetailRepository(apiService, db.mPhotoDetailDao())
        val viewModelFactory = PhotoDetailViewModelFactory(repository)
        photoDetailViewModel = ViewModelProvider(this, viewModelFactory)[PhotoDetailViewModel::class.java]
        loginUserProfileViewModel = ViewModelProvider(this)[LoginUserProfileViewModel::class.java]
        onItemClickListener = this

        binding.imgLogout.setOnClickListener {
            callLogoutFunction()
        }

        lifecycleScope.launch {
            if(NetworkUtils.isNetworkConnected(this@HomeActivity)){
                photoDetailViewModel.fetchFeeds()
            }else{
                CommonUtils.alertDialog(this@HomeActivity, resources.getString(R.string.check_internet_connection))
            }
        }

        photoDetailViewModel.allFeeds.observe(this) { feeds ->
            // Update your UI here with the observed posts
            setRecyclerView(feeds)
//            Log.d("FeedList", feeds.toString())
        }
    }

    private fun setRecyclerView(feeds: List<PhotoDetail>) {
        imageAdapter = ImageAdapter(feeds,onItemClickListener)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerFeed.layoutManager = layoutManager
        binding.recyclerFeed.adapter = imageAdapter
    }

    private fun callLogoutFunction() {
        loginUserProfileViewModel.deleteUserProfile()
        startActivity(Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
        finishAffinity()
    }

    private fun openDetailsPage(photoDetail: PhotoDetail?) {
        val gson = Gson()
        val photoDetailJson = gson.toJson(photoDetail)
        // Launch the details activity with the selected image URL
        val intent = Intent(this, PhotoDetailsActivity::class.java)
        intent.putExtra("photo_details", photoDetailJson)
        startActivity(intent)
    }


    override fun onItemClick(photoDetail: PhotoDetail?) {
        super.onItemClick(photoDetail)
        openDetailsPage(photoDetail)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
//        imageAdapter!!.notifyDataSetChanged()
    }
}