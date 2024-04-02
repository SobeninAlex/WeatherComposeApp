package com.example.weathercomposeapp.data.repository

import com.example.weathercomposeapp.data.mapper.toListEntity
import com.example.weathercomposeapp.data.network.api.ApiService
import com.example.weathercomposeapp.domain.entity.City
import com.example.weathercomposeapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {

    override suspend fun search(query: String): List<City> =
        apiService.searchCity(query = query).toListEntity()

}