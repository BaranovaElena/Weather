package com.example.weather.domain.repo.weather

import com.example.weather.domain.model.Weather
import com.example.weather.domain.model.WeatherDTO

interface WeathersRepo {
    fun getWeatherOfRusCities(): List<Weather>
    fun getWeatherOfWorldCities(): List<Weather>
    fun getWeatherOfCity(listener: WeatherLoaderListener, lat: Double, lon: Double)
}
interface WeatherLoaderListener {
    fun onLoaded(weatherDTO: WeatherDTO)
    fun onFailed(throwable: Throwable)
}