package com.example.weather.domain.repo.weather

import android.os.SystemClock
import android.util.Log
import com.example.weather.domain.repo.city.CitiesRepoImplDummy.NoCityFoundException
import com.example.weather.domain.model.City
import com.example.weather.domain.model.Weather
import com.example.weather.domain.model.WeatherDTO
import com.example.weather.domain.repo.city.lists.CityListsRepo
import com.example.weather.domain.repo.city.lists.CityListsRepoRus

class WeathersRepoImplDummy : WeathersRepo {
    private val cityListsRepo: CityListsRepo = CityListsRepoRus()
    private val worldCitiesWeather: List<Weather> = setCitiesWeather(true)
    private val rusCitiesWeather: List<Weather> = setCitiesWeather(false)

    private fun setCitiesWeather(isWorld: Boolean): List<Weather> {
        val weathers: MutableList<Weather> = mutableListOf()
        val cities: List<City> = when (isWorld) {
            true -> {
                cityListsRepo.getWorldCitiesList()
            }
            false -> {
                cityListsRepo.getLocalCitiesList()
            }
        }
        val weatherDTO = WeatherDTO()

        for (city in cities) {
            weathers.add(Weather(city, weatherDTO))
        }
        return weathers
    }

    override fun getWeatherOfRusCities() = rusCitiesWeather

    override fun getWeatherOfWorldCities() = worldCitiesWeather

    fun getWeatherOfCity(lat: Double, lon: Double, listener: WeatherLoaderListener) {
        return try {
            Thread {
                SystemClock.sleep(1000)
                val city = City("", lat, lon)
                listener.onLoaded(getWeatherOfCity(city))
            }.start()
        } catch (e: NoCityFoundException) {
            Log.d("@@@", e.message)
            listener.onFailed(e)
        }
    }

    private fun getWeatherOfCity(city: City): WeatherDTO {
        for (weather in rusCitiesWeather) {
            if (weather.city == city) {
                return weather.weatherDTO
            }
        }
        for (weather in worldCitiesWeather) {
            if (weather.city == city) {
                return weather.weatherDTO
            }
        }
        throw NoCityFoundException()
    }
}