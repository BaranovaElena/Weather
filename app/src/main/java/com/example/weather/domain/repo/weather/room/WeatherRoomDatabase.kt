package com.example.weather.domain.repo.weather.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherRoomEntity::class], version = 1)
abstract class WeatherRoomDatabase : RoomDatabase() {
    abstract fun weatherRoomDao(): WeatherRoomDao
}