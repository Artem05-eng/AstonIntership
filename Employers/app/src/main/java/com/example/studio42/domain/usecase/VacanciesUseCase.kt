package com.example.studio42.domain.usecase

import com.example.studio42.domain.repository.Repository
import javax.inject.Inject

class VacanciesUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id: String) = repository.getVacancies(id)
}