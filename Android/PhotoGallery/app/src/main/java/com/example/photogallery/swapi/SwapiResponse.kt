package com.example.photogallery.swapi

import com.example.photogallery.PhotoResponse
import com.google.gson.annotations.SerializedName

class PeopleResponse {
    @SerializedName("results")
    lateinit var peoples: List<PeopleItem>
}