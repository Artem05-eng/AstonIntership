package com.example.studio42.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studio42.domain.entity.EmloyerType
import com.example.studio42.domain.usecase.CacheUseCase
import com.example.studio42.domain.usecase.StartUseCase
import com.example.studio42.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartViewModel @Inject constructor(
    private val startUseCase: StartUseCase,
    private val cacheUseCase: CacheUseCase
) : ViewModel() {

    private val mutableData = MutableLiveData<List<EmloyerType>>()
    val data : LiveData<List<EmloyerType>>
        get() = mutableData
    private val mutableError = SingleLiveEvent<Unit>()
    val error : LiveData<Unit>
        get() = mutableError

    fun getEmploerType() {
        viewModelScope.launch {
            try {
                mutableData.postValue(startUseCase()!!)
            } catch (t: Throwable) {
                mutableData.postValue(cacheUseCase())
            } finally {
                if (mutableData.value.isNullOrEmpty()) {
                    mutableError.postValue(Unit)
                }
            }
        }
    }
}