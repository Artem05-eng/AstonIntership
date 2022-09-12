package com.example.studio42.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studio42.domain.entity.EmloyerType
import com.example.studio42.domain.usecase.CacheUseCase
import javax.inject.Inject

class ThirdViewModel @Inject constructor(
    private val cacheUseCase: CacheUseCase
): ViewModel() {

    private val mutableCache = MutableLiveData<List<EmloyerType>>()
    val cache: LiveData<List<EmloyerType>>
        get() = mutableCache

    fun getEmployerTypes() {
        mutableCache.postValue(cacheUseCase())
    }
}