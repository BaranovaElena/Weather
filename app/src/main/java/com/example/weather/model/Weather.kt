package com.example.weather.model

data class Weather(
    val city: City = City(
        "Москва",
        55.755826,
        37.617299900000035
    ),
    val temperature: Int = 0,
    val feelsLike: Int = 0
)