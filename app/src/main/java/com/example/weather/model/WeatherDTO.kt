package com.example.weather.model

data class WeatherDTO(
    val fact: FactDTO?
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?,
    val wind_speed: Double?,
    val wind_dir: String?,
    val pressure_mm : Int?,
    val humidity: Int?
)

