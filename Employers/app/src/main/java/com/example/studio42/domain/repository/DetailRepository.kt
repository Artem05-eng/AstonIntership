package com.example.studio42.domain.repository

import com.example.studio42.domain.entity.DetailEmployer

interface DetailRepository {

    suspend fun getDetailEmployer(id: String): DetailEmployer
}