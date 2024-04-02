package com.example.weathercomposeapp.domain.usecase

import com.example.weathercomposeapp.domain.entity.City
import com.example.weathercomposeapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class ChangeFavouriteStateUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    suspend fun addToFavourite(city: City) = repository.addToFavourite(city = city)

    suspend fun removeFromFavourite(cityId: Long) = repository.removeFromFavourite(cityId = cityId)

}