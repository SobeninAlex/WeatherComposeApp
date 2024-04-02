package com.example.weathercomposeapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto (
    @SerializedName("current") val current: WeatherDto,
    @SerializedName("location") val location: LocationDto,
    @SerializedName("forecast") val forecast: ForecastDto
)