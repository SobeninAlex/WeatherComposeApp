package com.example.weathercomposeapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ConditionDto(
    @SerializedName("icon") val icon: String,
    @SerializedName("text") val text: String
) {

    val correctImageUrl: String
        get() {
            return "https:$icon".replace("64x64", "128x128")
        }

}