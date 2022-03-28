package com.example.photogallery.api

import com.example.photogallery.FlickrResponse
import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=e0f14628bc9c8e497ea2969fee2cd382&format=json&nojsoncallback=1&extras=url_s")
    fun fetchPhotos(): Call<FlickrResponse>
}