package com.example.studio42.domain.usecase

import com.example.studio42.domain.repository.DetailRepository
import javax.inject.Inject

class GetDetailEmployerUseCase @Inject constructor(
    private val repository: DetailRepository
) {

    suspend operator fun invoke(id: String) = repository.getDetailEmployer(id)
}