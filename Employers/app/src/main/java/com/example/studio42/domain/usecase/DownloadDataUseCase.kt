package com.example.studio42.domain.usecase

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.studio42.data.datasource.database.EmployerDatabase
import com.example.studio42.data.datasource.mediator.EmployerRemoteMediator
import com.example.studio42.data.datasource.network.EmployerNetworkDataSource
import com.example.studio42.domain.entity.Employer
import com.example.studio42.domain.entity.RequestEmployer
import com.example.studio42.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadDataUseCase @Inject constructor(
    private val db: EmployerDatabase,
    private val network: EmployerNetworkDataSource,
) {

    @OptIn(ExperimentalPagingApi::class)
    operator fun invoke(
        requestEmployer: RequestEmployer,
        list: MutableLiveData<List<Employer>>,
        count: MutableLiveData<Int>
    ): Flow<PagingData<Employer>> {
        return Pager(
            config = PagingConfig(10, enablePlaceholders = false),
            remoteMediator = EmployerRemoteMediator(
                db = db,
                count = count,
                network = network,
                list = list,
                requestEmployer = requestEmployer,
            ),
            pagingSourceFactory = {db.employerDao().getPagingEmployers()}
        ).flow
    }
}