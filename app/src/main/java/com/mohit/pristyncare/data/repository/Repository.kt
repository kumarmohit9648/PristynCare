package com.mohit.pristyncare.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mohit.pristyncare.IMAGE_PER_PAGE
import com.mohit.pristyncare.data.api.Api
import com.mohit.pristyncare.model.Photo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class Repository @Inject constructor(private val _api: Api) {

    lateinit var imagesPagedList: LiveData<PagedList<Photo>>
    lateinit var imagesDataSourceFactory: ImagesDataSourceFactory

    fun fetchImagesPagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Photo>> {
        imagesDataSourceFactory = ImagesDataSourceFactory(_api, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(IMAGE_PER_PAGE)
            .build()

        imagesPagedList = LivePagedListBuilder(imagesDataSourceFactory, config).build()

        return imagesPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<ImagesDataSource, NetworkState>(
            imagesDataSourceFactory.imagesLiveDataSource, ImagesDataSource::networkState
        )
    }

}