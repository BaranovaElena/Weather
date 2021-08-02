package com.example.weather.model

interface WeathersRepo {
    fun getWeatherOfRusCities(): List<Weather>
    fun getWeatherOfWorldCities(): List<Weather>
    fun getWeatherOfCity(lat: Double, lon: Double): Weather
    fun getWeatherOfDefaultCity(): Weather
}
interface WeatherLoaderListener {
    fun onLoaded(weatherDTO: WeatherDTO)
    fun onFailed(throwable: Throwable)
}