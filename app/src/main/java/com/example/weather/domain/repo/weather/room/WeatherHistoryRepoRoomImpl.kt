package com.example.weather.domain.repo.weather.room

import android.os.Handler
import com.example.weather.domain.model.*

class WeatherHistoryRepoRoomImpl(
    private val roomDao: WeatherRoomDao,
    private val handler: Handler
) : WeatherHistoryRepo {
    override fun getAllHistory(onLoaderListener: HistoryLoaderListener) {
        Thread {
            val weatherHistoryList = roomDao.getAll().map { it.toWeather() }
            handler.post { onLoaderListener.onLoaded(weatherHistoryList) }
        }.start()
    }

    override fun saveEntity(weather: Weather) {
        Thread {
            roomDao.insertWeather(weather.toRoomEntity())
        }.start()
    }

    private fun WeatherRoomEntity.toWeather(): Weather {
        return Weather(
            City(this.cityName, 0.0, 0.0),
            WeatherDTO(FactDTO(this.temperature, 0, this.condition))
        )
    }

    private fun Weather.toRoomEntity(): WeatherRoomEntity {
        return WeatherRoomEntity(
            this.city.name,
            this.weatherDTO.fact?.temperature,
            this.weatherDTO.fact?.condition
        )
    }
}