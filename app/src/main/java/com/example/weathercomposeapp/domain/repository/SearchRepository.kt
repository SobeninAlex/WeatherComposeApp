package com.example.weathercomposeapp.domain.repository

import com.example.weathercomposeapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>

}