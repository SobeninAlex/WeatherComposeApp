package com.example.weathercomposeapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites_cities")
data class CityDbModel(
    @PrimaryKey val id: Long,
    val country: String,
    val city: String,
)