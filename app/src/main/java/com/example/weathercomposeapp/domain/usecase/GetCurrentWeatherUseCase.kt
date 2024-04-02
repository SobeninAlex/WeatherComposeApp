package com.example.weathercomposeapp.domain.usecase

import com.example.weathercomposeapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(cityId: Long) = repository.getWeather(cityId = cityId)

}