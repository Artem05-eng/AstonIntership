package com.example.studio42.domain.usecase

import com.example.studio42.domain.entity.RequestEmployer
import com.example.studio42.domain.repository.Repository
import javax.inject.Inject

class SearchLocalEmployerUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(requestEmployer: RequestEmployer) = repository.searchLocalEmployer(requestEmployer)
}