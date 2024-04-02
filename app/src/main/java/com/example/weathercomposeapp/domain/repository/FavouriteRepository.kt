package com.example.weathercomposeapp.domain.repository

import com.example.weathercomposeapp.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    val favouriteCities: Flow<List<City>>

    fun observeIsFavourite(cityId: Long): Flow<Boolean>

    suspend fun addToFavourite(city: City)

    suspend fun removeFromFavourite(cityId: Long)

}