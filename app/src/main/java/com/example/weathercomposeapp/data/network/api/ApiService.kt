package com.example.weathercomposeapp.data.network.api

import com.example.weathercomposeapp.data.network.dto.LocationDto
import com.example.weathercomposeapp.data.network.dto.WeatherCurrentDto
import com.example.weathercomposeapp.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("search.json")
    suspend fun searchCity(
        @Query("q") query: String,
    ): List<LocationDto>

    @GET("current.json?aqi=no")
    suspend fun loadCurrentWeather(
        @Query("q") query: String,
    ) : WeatherCurrentDto

    @GET("forecast.json?aqi=no&alerts=no")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") daysCount: Int = 4,
    ) : WeatherForecastDto

}