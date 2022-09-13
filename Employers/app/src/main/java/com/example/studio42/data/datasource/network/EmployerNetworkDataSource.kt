package com.example.studio42.data.datasource.network

import com.example.studio42.domain.entity.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EmployerNetworkDataSource {

    @GET("/employers")
    suspend fun getEmloyers(
        @Query("text") text: String,
        @Query("type", encoded = true) type: String,
        @Query("only_with_vacancies") only_with_vacancies: Boolean,
        @Query("per_page") per_page: Number = 10,
        @Query("page") page: Number
    ): EmployerFound

    @GET("/employers")
    suspend fun getEmployersLight(
        @Query("text") text: String,
        @Query("only_with_vacancies") only_with_vacancies: Boolean,
        @Query("per_page") per_page: Number = 10,
        @Query("page") page: Number
    ): EmployerFound

    @GET("/dictionaries")
    suspend fun getDetails(): EmployerTypeWrapper<EmloyerType>

    @GET("/employers/{employer_id}")
    suspend fun getEmployerById(
        @Path("employer_id") id: String
    ): DetailEmployer

    @GET("/vacancies")
    suspend fun getVacancies(
        @Query("employer_id") employer_id: String
    ): WrapperVacancy<Vacancy>
}