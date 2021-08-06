package com.example.weather.domain.repo.city

import com.example.weather.domain.model.City

interface CitiesRepo {
    fun getCityByCoordinates(lat: Double, lon: Double): City
    fun getDefaultCity(): City
    fun getCitiesListRus(): List<City>
    fun getCitiesListWorld(): List<City>
}