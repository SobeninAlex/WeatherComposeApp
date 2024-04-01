package com.example.weathercomposeapp.domain.usecase

import com.example.weathercomposeapp.domain.repository.FavouriteRepository
import javax.inject.Inject

class ObserveFavouriteStateCitiesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    operator fun invoke(cityId: Int) =
        repository.observeIsFavourite(cityId = cityId)

}