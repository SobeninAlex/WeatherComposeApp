package com.example.weathercomposeapp.presentation.search

import com.example.weathercomposeapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    //модель экрана
    val model: StateFlow<SearchStore.State>

    //действия пользователя
    fun changeSearchQuery(searchQuery: String)

    fun onClickBack()

    fun onClickSearch()

    fun onClickCity(city: City)

}