package com.mohit.pristyncare.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.mohit.pristyncare.data.api.Api
import com.mohit.pristyncare.model.Photo
import io.reactivex.disposables.CompositeDisposable

class ImagesDataSourceFactory(
    private val api: Api,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Photo>() {

    val imagesLiveDataSource = MutableLiveData<ImagesDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val imagesDataSource = ImagesDataSource(api, compositeDisposable)
        imagesLiveDataSource.postValue(imagesDataSource)
        return imagesDataSource
    }
}