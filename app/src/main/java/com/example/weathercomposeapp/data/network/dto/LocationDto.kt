package com.example.weathercomposeapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("id") val id: Long,
    @SerializedName("country") val country: String,
    @SerializedName("name") val city: String,
)