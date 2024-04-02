package com.example.weathercomposeapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("avgtemp_c") val avgTemp: Float,
    @SerializedName("condition") val condition: ConditionDto,
)
