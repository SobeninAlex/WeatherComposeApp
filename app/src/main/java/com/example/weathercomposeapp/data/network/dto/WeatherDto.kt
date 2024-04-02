package com.example.weathercomposeapp.data.network.dto

import com.google.gson.annotations.SerializedName
import java.util.Calendar
import java.util.Date

data class WeatherDto(
    @SerializedName("condition") val condition: ConditionDto,
    @SerializedName("temp_c") val tempC: Float,
    @SerializedName("last_updated_epoch") val date: Long,
)