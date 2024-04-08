package com.example.weathercomposeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.weathercomposeapp.WeatherApp
import com.example.weathercomposeapp.domain.usecase.ChangeFavouriteStateUseCase
import com.example.weathercomposeapp.domain.usecase.GetCurrentWeatherUseCase
import com.example.weathercomposeapp.domain.usecase.SearchCitiesUseCase
import com.example.weathercomposeapp.presentation.root.DefaultRootComponent
import com.example.weathercomposeapp.presentation.root.RootContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory


    private val appComponent by lazy {
        (applicationContext as WeatherApp).applicationComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        val root = rootComponentFactory.create(defaultComponentContext())

        setContent {
            RootContent(component = root)
        }
    }

}