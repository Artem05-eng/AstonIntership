package com.example.studio42.domain.usecase

import com.example.studio42.domain.repository.Repository
import javax.inject.Inject

class CacheUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke() = repository.getDataFromShared()
}