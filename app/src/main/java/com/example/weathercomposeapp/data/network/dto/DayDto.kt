package com.example.weathercomposeapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("day") val day: DayWeatherDto,
    @SerializedName("day_epoch") val date: Long,
)
