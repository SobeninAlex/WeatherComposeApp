package com.example.weathercomposeapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weathercomposeapp.data.network.api.ApiFactory
import com.example.weathercomposeapp.presentation.ui.theme.WeatherComposeAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = ApiFactory.apiService

        CoroutineScope(Dispatchers.IO).launch {
            val current = apiService.loadCurrentWeather(query = "London")
            val forecast = apiService.loadForecast(query = "London")
            val search = apiService.searchCity(query = "London")
            Log.d("MainActivityTag", current.toString())
            Log.d("MainActivityTag", forecast.toString())
            Log.d("MainActivityTag", search.toString())
        }

        setContent {
            WeatherComposeAppTheme {

            }
        }

    }
}