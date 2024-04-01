package com.example.weathercomposeapp.domain.repository

import com.example.weathercomposeapp.domain.entity.Forecast
import com.example.weathercomposeapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast

}