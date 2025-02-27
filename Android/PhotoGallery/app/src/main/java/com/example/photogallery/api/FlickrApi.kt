package com.example.photogallery.api

import com.example.photogallery.FlickrResponse
import okhttp3.ResponseBody
import com.example.photogallery.swapi.PeopleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {

    @GET("services/rest/?method=flickr.interestingness.getList")
    fun fetchPhotos(): Call<FlickrResponse>

    @GET("api/people")
    fun fetchPeoples(): Call<PeopleResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    @GET("/services/rest?method=flickr.photos.search")
    fun searchPhotos(@Query("text") query: String) : Call<FlickrResponse>
}