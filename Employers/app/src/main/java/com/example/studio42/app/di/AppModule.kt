package com.example.studio42.app.di

import android.content.Context
import com.example.studio42.app.App
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesContext(app: App): Context = app.applicationContext
}