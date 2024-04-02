package com.example.weathercomposeapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("condition") val condition: ConditionDto,
    @SerializedName("temp_c") val temp: Float,
    @SerializedName("last_updated_epoch") val date: Long,
)