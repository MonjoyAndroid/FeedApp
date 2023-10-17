package com.monjoy.feedapp.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson
import com.monjoy.feedapp.R
import com.monjoy.feedapp.databinding.ActivityHomeBinding
import com.monjoy.feedapp.databinding.ActivityPhotoDetailsBinding
import com.monjoy.feedapp.network.PhotoDetailRepository
import com.monjoy.feedapp.retrofit.ApiService
import com.monjoy.feedapp.retrofit.RetrofitClient
import com.monjoy.feedapp.room.FeedDatabase
import com.monjoy.feedapp.room.entity.PhotoDetail
import com.monjoy.feedapp.room.viewmodel.PhotoDetailViewModel
import com.monjoy.feedapp.room.viewmodel.PhotoDetailViewModelFactory

class PhotoDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoDetailsBinding
    private lateinit var photoDetailViewModel: PhotoDetailViewModel
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = FeedDatabase.getDatabase(this)!!
        apiService = RetrofitClient.createService(ApiService::class.java)
        val repository = PhotoDetailRepository(apiService, db.mPhotoDetailDao())
        val viewModelFactory = PhotoDetailViewModelFactory(repository)
        photoDetailViewModel = ViewModelProvider(this, viewModelFactory)[PhotoDetailViewModel::class.java]
        setDetailsView()
    }

    @SuppressLint("SetTextI18n")
    private fun setDetailsView() {
        val photoDetailJson = intent.getStringExtra("photo_details")
        val gson = Gson()
        val photoDetail = gson.fromJson(photoDetailJson, PhotoDetail::class.java)
        binding.tvId.text = "Photo Id: ${photoDetail.id}"
        binding.tvAlbumId.text = "Album Id: ${photoDetail.albumId}"
        binding.tvFeedName.text = "Title: ${photoDetail.title}"
        Glide.with(this)
            .load(photoDetail.url)
            .placeholder(R.drawable.icon_placeholder) // Placeholder image resource
            .error(R.drawable.icon_error_image_load) // Error image resource
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imgFeedItem)

        if(photoDetail.isLiked){
            binding.imgLike.setImageResource(R.drawable.icon_liked)
        }else{
            binding.imgLike.setImageResource(R.drawable.icon_unliked)
        }


        binding.imgArrowBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgLike.setOnClickListener {
            if(photoDetail.isLiked){
                binding.imgLike.setImageResource(R.drawable.icon_unliked)
                photoDetail.isLiked = false
            }else{
                binding.imgLike.setImageResource(R.drawable.icon_liked)
                photoDetail.isLiked = true
            }
            photoDetailViewModel.updateLikeStatusById(photoDetail.id, photoDetail.isLiked)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}