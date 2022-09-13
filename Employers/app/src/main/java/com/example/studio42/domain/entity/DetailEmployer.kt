package com.example.studio42.domain.entity

data class DetailEmployer(
    val id: String,
    val name: String,
    val open_vacancies: Number,
    val site_url: String,
    val trusted: Boolean,
    val type: String?,
    val logo_urls: LogoUrls,
    val area: Area,
    val vacancies_url: String,
    val industries: List<Industries>,
    val description: String?
)

data class Industries(
    val id: String,
    val name: String
)

data class LogoUrls(
    val original: String?
)

data class Area(
    val id: String,
    val name: String
)

data class Vacancy(
    val id: String,
    val name: String,
    val area: Area,
    val has_test: Boolean,
    val published_at: String,
    val alternate_url: String
)

data class WrapperVacancy<T>(
    val items: List<T>
)