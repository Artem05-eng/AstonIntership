package com.example.studio42.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studio42.domain.entity.Vacancy
import com.example.studio42.domain.usecase.VacanciesUseCase
import com.example.studio42.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class VacancyViewModel @Inject constructor(
    private val vacanciesUseCase: VacanciesUseCase
): ViewModel() {

    private val mutableData = MutableLiveData<List<Vacancy>>()
    val data: LiveData<List<Vacancy>>
        get() = mutableData

    private val mutableError = SingleLiveEvent<Unit>()
    val error : LiveData<Unit>
        get() = mutableError

    fun getDetail(id: String) {
        viewModelScope.launch {
            try {
                val data = vacanciesUseCase(id)
                mutableData.postValue(data)
            } catch (t: Throwable) {
                mutableError.postValue(Unit)
            }
        }
    }
}