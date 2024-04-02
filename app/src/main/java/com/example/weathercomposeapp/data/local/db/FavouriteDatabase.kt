package com.example.weathercomposeapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weathercomposeapp.data.local.model.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {

    abstract fun favouriteCitiesDao(): FavouriteCitiesDao

    /**
     * singleton double check
     */
    companion object {

        private var INSTANCE: FavouriteDatabase? = null
        private val LOCK = Any()
        private const val NAME_DB = "FavouriteDatabase"

        fun getInstance(context: Context) : FavouriteDatabase {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }

                val database = Room.databaseBuilder(
                    context = context,
                    klass = FavouriteDatabase::class.java,
                    name = NAME_DB
                ).build()

                INSTANCE = database
                return database
            }
        }

    }

}