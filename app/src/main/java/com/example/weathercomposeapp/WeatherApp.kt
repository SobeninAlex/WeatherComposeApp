package com.example.weathercomposeapp

import android.app.Application
import com.example.weathercomposeapp.di.ApplicationComponent
import com.example.weathercomposeapp.di.DaggerApplicationComponent

class WeatherApp : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }

}