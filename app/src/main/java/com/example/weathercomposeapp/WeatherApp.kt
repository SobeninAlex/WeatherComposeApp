package com.example.weathercomposeapp

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.weathercomposeapp.di.ApplicationComponent
import com.example.weathercomposeapp.di.DaggerApplicationComponent

class WeatherApp : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }

}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as WeatherApp).component
}