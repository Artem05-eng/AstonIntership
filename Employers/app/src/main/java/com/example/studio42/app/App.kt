package com.example.studio42.app

import android.app.Application
import com.example.studio42.di.AppComponent
import com.example.studio42.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Any>
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = (DaggerAppComponent.factory().create(this) as AppComponent)
        component.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = activityInjector

    companion object {
        const val SHAREDPREFS_NAME = "prefs"
    }
}