package com.example.studio42.data.repository

import com.example.studio42.data.datasource.network.EmployerNetworkDataSource
import com.example.studio42.domain.entity.DetailEmployer
import com.example.studio42.domain.repository.DetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val networkDataSource: EmployerNetworkDataSource
) : DetailRepository {

    override suspend fun getDetailEmployer(id: String): DetailEmployer = withContext(Dispatchers.IO) {
        networkDataSource.getEmployerById(id)
    }
}