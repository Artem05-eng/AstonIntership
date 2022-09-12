package com.example.studio42.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studio42.domain.entity.Employer
import com.example.studio42.domain.entity.RequestEmployer
import com.example.studio42.domain.usecase.DownloadDataUseCase
import com.example.studio42.domain.usecase.SearchLocalEmployerUseCase
import javax.inject.Inject

class SecondViewModel @Inject constructor(
    private val downloadUseCase: DownloadDataUseCase,
    private val searchLocalEmployerUseCase: SearchLocalEmployerUseCase
) : ViewModel() {
    private val mutableData = MutableLiveData<List<Employer>>()
    val data: LiveData<List<Employer>>
        get() = mutableData
    private val mutableFlagFilter = MutableLiveData<Boolean>(false)
    val flagFilter: LiveData<Boolean>
        get() = mutableFlagFilter
    private val mutableCount = MutableLiveData<Int>()
    val count: LiveData<Int>
        get() = mutableCount


    fun getEmploers(requestEmployer: RequestEmployer, isOnline: Boolean) =
        if (isOnline) {
            downloadUseCase(requestEmployer, mutableData, mutableCount)
        } else {
            searchLocalEmployerUseCase(requestEmployer)
        }

    fun checkFlag() {
        mutableFlagFilter.postValue(true)
    }

    fun uncheckFlag() {
        mutableFlagFilter.postValue(false)
    }

}