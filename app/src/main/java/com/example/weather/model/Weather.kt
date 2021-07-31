package com.example.weather.model

data class Weather(
    val city: City = City(),
    val temperature: Int = 0,
    val feelsLike: Int = 0
)
