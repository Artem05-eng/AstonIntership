package com.example.studio42.ui.di

import com.example.studio42.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindFirstFragment(): FirstFragment

    @ContributesAndroidInjector
    abstract fun bindSecondFragment(): SecondFragment

    @ContributesAndroidInjector
    abstract fun bindThirdFragment(): ThirdFragment

    @ContributesAndroidInjector
    abstract fun bindDetailFragment(): DetailFragment

    @ContributesAndroidInjector
    abstract fun bindVacanciesFragment(): VacancyFragment
}