package com.example.studio42.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studio42.domain.entity.DetailEmployer
import com.example.studio42.domain.usecase.GetDetailEmployerUseCase
import com.example.studio42.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val networkUseCase: GetDetailEmployerUseCase
) : ViewModel() {
    private val mutableDetail = MutableLiveData<DetailEmployer>()
    val detail : LiveData<DetailEmployer>
        get() = mutableDetail
    private val mutableError = SingleLiveEvent<Unit>()
    val error : LiveData<Unit>
        get() = mutableError

    fun getDetail(id: String) {
        viewModelScope.launch {
            try {
                val data = networkUseCase(id)
                mutableDetail.postValue(data)
            } catch (t: Throwable) {
                mutableError.postValue(Unit)
            }
        }
    }
}