package com.example.mirela.rxjava.DI

import android.app.Application

open class LaunchApplication: Application() {
     var appComponent: AppComponent = DaggerAppComponent.builder().repositoryModule(RepositoryModule()).build()

    override fun onCreate() {
        super.onCreate()
    }

}