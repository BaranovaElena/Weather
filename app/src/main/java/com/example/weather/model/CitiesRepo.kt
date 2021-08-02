package com.example.weather.model

interface CitiesRepo {
    fun getCityByCoordinates(lat: Double, lon: Double): City
    fun getDefaultCity(): City
    fun getCitiesListRus(): List<City>
    fun getCitiesListWorld(): List<City>
}