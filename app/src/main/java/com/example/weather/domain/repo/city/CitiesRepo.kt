package com.example.weather.domain.repo.city

import android.content.Context
import com.example.weather.domain.model.City

interface CitiesRepo {
    fun getCityByCoordinates(context: Context, lat: Double, lon: Double, listener: CityLoaderListener)
    fun getDefaultCity(context: Context, listener: CityLoaderListener)
}

interface CityLoaderListener {
    fun onLoaded(city: City)
    fun onFailed(throwable: Throwable)
}