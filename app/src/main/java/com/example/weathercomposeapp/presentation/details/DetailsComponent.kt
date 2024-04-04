package com.example.weathercomposeapp.presentation.details

import com.example.weathercomposeapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    //модель экрана
    val model: StateFlow<DetailsStore.State>

    //действия пользователя
    fun onClickBack()

    fun onClickChangeFavouriteStatus()

}