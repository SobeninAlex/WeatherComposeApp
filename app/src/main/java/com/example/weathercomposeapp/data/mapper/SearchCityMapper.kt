package com.example.weathercomposeapp.data.mapper

import com.example.weathercomposeapp.data.network.dto.LocationDto
import com.example.weathercomposeapp.domain.entity.City

fun LocationDto.toEntity(): City = City(id, city, country)

fun List<LocationDto>.toListEntity(): List<City> = map {
    it.toEntity()
}

