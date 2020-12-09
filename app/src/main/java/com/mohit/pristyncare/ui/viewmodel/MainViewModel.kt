package com.mohit.pristyncare.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.mohit.pristyncare.data.repository.NetworkState
import com.mohit.pristyncare.data.repository.Repository
import com.mohit.pristyncare.model.Photo
import io.reactivex.disposables.CompositeDisposable

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val imagePagedList: LiveData<PagedList<Photo>> by lazy {
        repository.fetchImagesPagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return imagePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}