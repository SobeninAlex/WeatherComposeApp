package com.example.weathercomposeapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weathercomposeapp.data.local.model.CityDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCitiesDao {

    @Query("select * from favourites_cities")
    fun getFavouriteCities(): Flow<List<CityDbModel>>

    @Query("select exists (select * from favourites_cities where id = :cityId limit 1)")
    fun observeIsFavourite(cityId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourite(cityDbModel: CityDbModel)

    @Query("delete from favourites_cities where id = :cityId")
    suspend fun removeFromFavourite(cityId: Long)

}