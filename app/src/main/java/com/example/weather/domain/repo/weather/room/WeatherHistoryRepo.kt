package com.example.weather.domain.repo.weather.room

import com.example.weather.domain.model.Weather

interface WeatherHistoryRepo {
    fun getAllHistory(onLoaderListener: HistoryLoaderListener)
    fun saveEntity(weather: Weather)
}

interface HistoryLoaderListener {
    fun onLoaded(weatherList: List<Weather>)
    fun onFailed(throwable: Throwable)
}