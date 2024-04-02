package com.example.weathercomposeapp.di.module

import android.content.Context
import com.example.weathercomposeapp.data.local.db.FavouriteCitiesDao
import com.example.weathercomposeapp.data.local.db.FavouriteDatabase
import com.example.weathercomposeapp.data.network.api.ApiFactory
import com.example.weathercomposeapp.data.network.api.ApiService
import com.example.weathercomposeapp.data.repository.FavouriteRepositoryImpl
import com.example.weathercomposeapp.data.repository.SearchRepositoryImpl
import com.example.weathercomposeapp.data.repository.WeatherRepositoryImpl
import com.example.weathercomposeapp.di.annotation.ApplicationScope
import com.example.weathercomposeapp.domain.repository.FavouriteRepository
import com.example.weathercomposeapp.domain.repository.SearchRepository
import com.example.weathercomposeapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @[ApplicationScope Provides]
        fun provideFavouriteDatabase(context: Context): FavouriteDatabase {
            return FavouriteDatabase.getInstance(context)
        }

        @[ApplicationScope Provides]
        fun provideFavouriteCitiesDao(favouriteDatabase: FavouriteDatabase): FavouriteCitiesDao {
            return favouriteDatabase.favouriteCitiesDao()
        }

    }

}