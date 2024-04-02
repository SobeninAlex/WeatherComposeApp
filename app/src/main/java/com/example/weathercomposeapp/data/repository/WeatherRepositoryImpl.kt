package com.example.weathercomposeapp.data.repository

import com.example.weathercomposeapp.data.mapper.toEntity
import com.example.weathercomposeapp.data.network.api.ApiService
import com.example.weathercomposeapp.domain.entity.Forecast
import com.example.weathercomposeapp.domain.entity.Weather
import com.example.weathercomposeapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun getWeather(cityId: Long): Weather =
        apiService.loadCurrentWeather(query = "$PREFIX_CITY_ID$cityId").toEntity()

    override suspend fun getForecast(cityId: Long): Forecast =
        apiService.loadForecast(query = "$PREFIX_CITY_ID$cityId").toEntity()

    private companion object {
        private const val PREFIX_CITY_ID = "id:"
    }
}