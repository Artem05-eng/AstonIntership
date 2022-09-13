package com.example.studio42.data.repository

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.studio42.data.datasource.database.EmployerDao
import com.example.studio42.data.datasource.network.EmployerNetworkDataSource
import com.example.studio42.domain.entity.EmloyerType
import com.example.studio42.domain.entity.Employer
import com.example.studio42.domain.entity.EmployerFound
import com.example.studio42.domain.entity.RequestEmployer
import com.example.studio42.domain.repository.Repository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val network: EmployerNetworkDataSource,
    private val shared: SharedPreferences,
    private val db: EmployerDao
) : Repository {

    private companion object {
        const val LOCALE_KEY = "222"
    }

    override suspend fun getEmloyerType(): List<EmloyerType> = network.getDetails().employer_type


    override fun getDataFromShared(): List<EmloyerType> {
        val json = shared.getString(LOCALE_KEY, null)
        val listType = object : TypeToken<List<EmloyerType>>() {}.type
        return Gson().fromJson<List<EmloyerType>>(json, listType) ?: emptyList()
    }

    override fun saveDataToShared(data: List<EmloyerType>) {
        val string = Gson().toJson(data)
        shared.edit().putString(LOCALE_KEY, string).apply()
    }

    override fun searchLocalEmployer(request: RequestEmployer): Flow<PagingData<Employer>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { db.searchPagingEmployers(request.text) }
        ).flow
    }


}