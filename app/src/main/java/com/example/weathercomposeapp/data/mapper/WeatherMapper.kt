package com.example.weathercomposeapp.data.mapper

import com.example.weathercomposeapp.data.network.dto.WeatherCurrentDto
import com.example.weathercomposeapp.data.network.dto.WeatherDto
import com.example.weathercomposeapp.data.network.dto.WeatherForecastDto
import com.example.weathercomposeapp.domain.entity.Forecast
import com.example.weathercomposeapp.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toEntity(): Weather = current.toEntity()

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    conditionText = condition.text,
    conditionUrl = condition.correctImageUrl,
    date = date.toCalendar()
)

fun WeatherForecastDto.toEntity(): Forecast = Forecast(
    currentWeather = current.toEntity(),
    upcoming = forecast.forecastDay.map {
        Weather(
            tempC = it.day.avgTemp,
            conditionText = it.day.condition.text,
            conditionUrl = it.day.condition.correctImageUrl,
            date = it.date.toCalendar()
        )
    }
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}
