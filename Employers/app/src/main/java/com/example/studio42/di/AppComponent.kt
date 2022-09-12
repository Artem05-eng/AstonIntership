package com.example.studio42.di

import com.example.studio42.app.App
import com.example.studio42.app.di.AppModule
import com.example.studio42.data.di.DataModule
import com.example.studio42.domain.di.DomainModule
import com.example.studio42.presentation.di.PresentationModule
import com.example.studio42.ui.di.UIModule
import dagger.Component
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    DataModule::class,
    DomainModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    PresentationModule::class,
    UIModule::class
])
interface AppComponent : AndroidInjector<App> {

    override fun inject(app: App)

    @Component.Factory
    interface Factory : AndroidInjector.Factory<App>
}