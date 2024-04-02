package com.example.weathercomposeapp.data.mapper

import com.example.weathercomposeapp.data.local.model.CityDbModel
import com.example.weathercomposeapp.domain.entity.City

fun City.toDbModel(): CityDbModel = CityDbModel(id, country, city)

fun CityDbModel.toEntity(): City = City(id, city, country)

fun List<CityDbModel>.toListEntity(): List<City> = map { it.toEntity() }