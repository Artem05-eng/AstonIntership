package com.example.studio42.domain.repository

import androidx.paging.PagingData
import com.example.studio42.data.repository.RepositoryImpl
import com.example.studio42.domain.entity.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getEmloyerType(): List<EmloyerType>

    /*suspend fun getEmployer(text: String, type: String?, flag: Boolean): EmployerFound*/

    fun getDataFromShared(): List<EmloyerType>

    fun saveDataToShared(data: List<EmloyerType>)

    fun searchLocalEmployer(request: RequestEmployer): Flow<PagingData<Employer>>

    suspend fun getVacancies(id: String): List<Vacancy>
}