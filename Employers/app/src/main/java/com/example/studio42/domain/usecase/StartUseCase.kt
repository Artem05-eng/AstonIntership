package com.example.studio42.domain.usecase

import com.example.studio42.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StartUseCase @Inject constructor(private val repository: Repository) {

    operator suspend fun invoke() = withContext(Dispatchers.IO) {
        val result = async { repository.getEmloyerType() }
        val data = result.await()
        repository.saveDataToShared(data)
        data
    }
}