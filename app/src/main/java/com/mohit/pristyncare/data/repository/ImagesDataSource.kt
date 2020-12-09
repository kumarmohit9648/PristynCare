package com.mohit.pristyncare.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.mohit.pristyncare.FIRST_PAGE
import com.mohit.pristyncare.data.api.Api
import com.mohit.pristyncare.model.Photo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ImagesDataSource(
    private val api: Api,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Photo>() {

    companion object {
        private const val TAG = "ImagesNetworkDataSource"
        private const val API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736"
    }

    private var page = FIRST_PAGE

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        _networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            api.getImages(API_KEY, page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.photos.photo, null, page + 1)
                        _networkState.postValue(NetworkState.LOADED)
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e(TAG, "loadInitial: ", it)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        _networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            api.getImages(API_KEY, params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.photos.pages >= params.key) {
                            callback.onResult(it.photos.photo, page + 1)
                            _networkState.postValue(NetworkState.LOADED)
                        } else {
                            _networkState.postValue(NetworkState.END_OF_LIST)
                        }
                    }, {
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e(TAG, "loadInitial: ", it)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {

    }

}