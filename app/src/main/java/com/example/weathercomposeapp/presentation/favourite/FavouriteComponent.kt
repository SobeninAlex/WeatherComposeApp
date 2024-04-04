package com.example.weathercomposeapp.presentation.favourite

import com.example.weathercomposeapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {

    // модель экрана
    val model: StateFlow<FavouriteStore.State>

    //действия, которые может совершать пользователь на экране
    fun onClickSearch()

    fun onClickAddFavourite()

    fun onCityItemClick(city: City)

}