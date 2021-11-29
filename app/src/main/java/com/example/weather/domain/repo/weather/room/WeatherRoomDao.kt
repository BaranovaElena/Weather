package com.example.weather.domain.repo.weather.room

import androidx.room.*

@Dao
interface WeatherRoomDao {

    @Query("SELECT * FROM weather_history_table")
    fun getAll(): List<WeatherRoomEntity>

    @Query("SELECT * FROM weather_history_table WHERE city LIKE :city")
    fun getWeatherByCity(city: String): List<WeatherRoomEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeather(entity: WeatherRoomEntity)

    @Update
    fun updateWeather(entity: WeatherRoomEntity)

    @Delete
    fun deleteWeather(entity: WeatherRoomEntity)

    @Query("DELETE FROM weather_history_table")
    fun deleteAll()
}