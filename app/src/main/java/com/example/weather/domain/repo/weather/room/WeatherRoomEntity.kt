package com.example.weather.domain.repo.weather.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_history_table")
data class WeatherRoomEntity(
    @PrimaryKey
    @ColumnInfo(name = "city") val cityName: String,
    @ColumnInfo(name = "temperature") val temperature: Int? = null,
    @ColumnInfo(name = "condition") val condition: String? = null
)
