package com.example.studio42.data.datasource.mediator

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.studio42.data.datasource.database.EmployerDatabase
import com.example.studio42.data.datasource.network.EmployerNetworkDataSource
import com.example.studio42.domain.entity.Employer
import com.example.studio42.domain.entity.RequestEmployer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

@ExperimentalPagingApi
class EmployerRemoteMediator(
    private val requestEmployer: RequestEmployer,
    private val list: MutableLiveData<List<Employer>>,
    private val count: MutableLiveData<Int>,
    private val db: EmployerDatabase,
    private val network: EmployerNetworkDataSource
) : RemoteMediator<Int, Employer>() {

    private val initailPage: Int = 1

    override suspend fun initialize(): InitializeAction {
        list.postValue(emptyList())
        count.postValue(0)
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Employer>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val employer = getClosestEmployer(state)
                employer?.next_key?.minus(1) ?: initailPage
            }
            LoadType.PREPEND -> {
                val employer = getEmployerForFirstItem(state)
                val prev_key = employer?.prev_key ?: return MediatorResult.Success(
                    endOfPaginationReached = employer != null
                )
                prev_key
            }
            LoadType.APPEND -> {
                val employer = getEmployerForLastItem(state)
                val next_key = employer?.next_key ?: return MediatorResult.Success(
                    endOfPaginationReached = employer != null
                )
                next_key
            }
        }
        try {
            val data = withContext(Dispatchers.IO) {
                if (requestEmployer.type != "") {
                    network.getEmloyers(
                        requestEmployer.text,
                        requestEmployer.type,
                        requestEmployer.flag,
                        page = page
                    )
                } else {
                    network.getEmployersLight(
                        requestEmployer.text,
                        requestEmployer.flag,
                        page = page
                    )
                }
            }
            val employers = data?.items
            list.postValue(employers)
            count.postValue(data?.found)
            val endOfPaginationReached = employers?.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.employerDao().clearEmployers()
                }
                val prevKey = if (page == initailPage) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val employersDB = employers.map { employer ->
                    employer.prev_key = prevKey
                    employer.next_key = nextKey
                    employer
                }
                db.employerDao().insertEmployers(employersDB)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Throwable) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getEmployerForLastItem(state: PagingState<Int, Employer>): Employer? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()?.let { employer ->
                db.employerDao().getEmployerById(employer.id)
            }
    }

    private suspend fun getEmployerForFirstItem(state: PagingState<Int, Employer>): Employer? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()?.let { employer ->
                db.employerDao().getEmployerById(employer.id)
            }
    }

    private suspend fun getClosestEmployer(state: PagingState<Int, Employer>): Employer? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { employerId ->
                db.employerDao().getEmployerById(employerId)
            }
        }
    }
}