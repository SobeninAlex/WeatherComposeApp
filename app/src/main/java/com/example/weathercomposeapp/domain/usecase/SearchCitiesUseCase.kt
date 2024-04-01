package com.example.weathercomposeapp.domain.usecase

import com.example.weathercomposeapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend operator fun invoke(query: String) = repository.search(query = query)

}