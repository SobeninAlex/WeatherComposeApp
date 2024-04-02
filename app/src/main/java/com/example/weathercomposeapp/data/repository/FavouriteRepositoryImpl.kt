package com.example.weathercomposeapp.data.repository

import com.example.weathercomposeapp.data.local.db.FavouriteCitiesDao
import com.example.weathercomposeapp.data.mapper.toDbModel
import com.example.weathercomposeapp.data.mapper.toListEntity
import com.example.weathercomposeapp.domain.entity.City
import com.example.weathercomposeapp.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val favouriteCitiesDao: FavouriteCitiesDao
) : FavouriteRepository {

    override val favouriteCities: Flow<List<City>> = favouriteCitiesDao.getFavouriteCities()
        .map { it.toListEntity() }

    override fun observeIsFavourite(cityId: Long): Flow<Boolean> =
        favouriteCitiesDao.observeIsFavourite(cityId = cityId)

    override suspend fun addToFavourite(city: City) {
        favouriteCitiesDao.addToFavourite(city.toDbModel())
    }

    override suspend fun removeFromFavourite(cityId: Long) {
        favouriteCitiesDao.removeFromFavourite(cityId = cityId)
    }

}