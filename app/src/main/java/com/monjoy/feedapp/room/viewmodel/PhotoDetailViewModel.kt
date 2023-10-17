package com.monjoy.feedapp.room.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.monjoy.feedapp.network.PhotoDetailRepository
import com.monjoy.feedapp.room.FeedDatabase
import com.monjoy.feedapp.room.entity.PhotoDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoDetailViewModel(private val repository: PhotoDetailRepository) : ViewModel()  {

    private val _allFeeds = MutableLiveData<List<PhotoDetail>>()
    val allFeeds: LiveData<List<PhotoDetail>> = _allFeeds

    suspend fun fetchFeeds() {
        repository.fetchFeeds()
        _allFeeds.value = repository.getFeedsFromDb()
    }

    fun updateLikeStatusById(id: Long, isLiked: Boolean) {
        viewModelScope.launch {
            repository.updateLikeStatusById(id, isLiked)
        }
    }

}

class PhotoDetailViewModelFactory(private val repository: PhotoDetailRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}