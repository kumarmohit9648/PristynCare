package com.mohit.pristyncare.data.api

import com.mohit.pristyncare.model.FlickrResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&safe_search=1&tags=kitten&per_page=10")
    fun getImages(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Single<FlickrResponse>

}