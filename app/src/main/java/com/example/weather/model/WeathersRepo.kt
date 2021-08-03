package com.example.weather.model

interface WeathersRepo {
    fun getWeatherOfRusCities(): List<Weather>
    fun getWeatherOfWorldCities(): List<Weather>
    fun getWeatherOfCity(listener: WeatherLoaderListener, lat: Double, lon: Double)
}
interface WeatherLoaderListener {
    fun onLoaded(weatherDTO: WeatherDTO)
    fun onFailed(throwable: Throwable)
}