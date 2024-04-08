package com.example.weathercomposeapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("day") val day: DayWeatherDto,
    @SerializedName("date_epoch") val date: Long,
)
