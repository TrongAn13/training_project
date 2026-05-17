package com.example.training_project

import android.app.Application
import com.example.training_project.di.appModule
import org.koin.android.ext.koin.androidContext

import org.koin.core.context.startKoin

class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApplication)
            modules(appModule)
        }
    }
}
