package com.example.studio42.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.studio42.app.App
import com.example.studio42.data.datasource.database.EmployerDao
import com.example.studio42.data.datasource.database.EmployerDatabase
import com.example.studio42.data.datasource.network.EmployerNetworkDataSource
import com.example.studio42.data.datasource.network.interceptor.CustomInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .addInterceptor(CustomInterceptor())
        .followRedirects(true)
        .build()

    @Singleton
    @Provides
    fun providesGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun providesNetworkApi(okHttpClient: OkHttpClient, gson: Gson): EmployerNetworkDataSource =
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create()

    @Singleton
    @Provides
    fun providesSharedPrefs(app: App): SharedPreferences =
        app.getSharedPreferences(App.SHAREDPREFS_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesDatabase(app: App): EmployerDatabase {
        return Room.databaseBuilder(
            app,
            EmployerDatabase::class.java,
            EmployerDatabase.DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun providesPhotoDao(db: EmployerDatabase): EmployerDao = db.employerDao()

}