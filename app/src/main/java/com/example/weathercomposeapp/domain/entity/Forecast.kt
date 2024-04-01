package com.example.weathercomposeapp.domain.entity

data class Forecast(
    val currentWeather: Weather,
    val upcoming: List<Weather>
)
