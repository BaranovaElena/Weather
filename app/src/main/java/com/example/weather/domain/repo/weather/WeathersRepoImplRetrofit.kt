package com.example.weather.domain.repo.weather

import com.example.weather.domain.model.Weather
import com.example.weather.domain.model.WeatherDTO
import com.example.weather.keys.YANDEX_WEATHER_API_KEY
import retrofit2.Callback
import retrofit2.Retrofit

class WeathersRepoImplRetrofit(private val retrofit: Retrofit) : WeathersRepo {
    private val service: WeatherRetrofitService by lazy { retrofit.create(WeatherRetrofitService::class.java) }

    override fun getWeatherOfRusCities(): List<Weather> {
        //реализация при необходимости выводить какие-то данные о погоде в общем списке городов
        return emptyList()
    }

    override fun getWeatherOfWorldCities(): List<Weather> {
        //реализация при необходимости выводить какие-то данные о погоде в общем списке городов
        return emptyList()
    }

    fun getWeatherOfCity(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        service.getWeather(YANDEX_WEATHER_API_KEY,lat,lon).enqueue(callback)
    }
}