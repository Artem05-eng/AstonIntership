package com.example.studio42.domain.di

import com.example.studio42.data.repository.DetailRepositoryImpl
import com.example.studio42.data.repository.RepositoryImpl
import com.example.studio42.domain.repository.DetailRepository
import com.example.studio42.domain.repository.Repository
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun providesRepository(impl: RepositoryImpl): Repository

    @Binds
    fun providesDetailRepository(impl: DetailRepositoryImpl): DetailRepository
}