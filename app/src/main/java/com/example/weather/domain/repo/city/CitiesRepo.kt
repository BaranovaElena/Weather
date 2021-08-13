package com.example.weather.domain.repo.city

import android.content.Context
import com.example.weather.domain.model.City

interface CitiesRepo {
    fun getCityByCoordinates(lat: Double, lon: Double): City
    fun getDefaultCity(context: Context, listener: CityLoaderListener)
    fun getCitiesListRus(): List<City>
    fun getCitiesListWorld(): List<City>
}

interface CityLoaderListener {
    fun onLoaded(city: City)
    fun onFailed(throwable: Throwable)
}