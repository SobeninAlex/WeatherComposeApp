package com.example.weathercomposeapp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Long,
    val city: String,
    val country: String
) : Parcelable
