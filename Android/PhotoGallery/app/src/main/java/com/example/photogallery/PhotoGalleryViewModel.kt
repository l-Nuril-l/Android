package com.example.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.photogallery.swapi.PeopleItem

class PhotoGalleryViewModel: ViewModel() {
    val galleryItemLiveData: LiveData<List<PeopleItem>>

    init {
        galleryItemLiveData = FlickrFetchr().fetchPeoples()
    }
}