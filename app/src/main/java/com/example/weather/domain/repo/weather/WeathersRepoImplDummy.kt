package com.example.weather.domain.repo.weather

import android.os.SystemClock
import android.util.Log
import com.example.weather.domain.repo.city.CitiesRepo
import com.example.weather.domain.repo.city.CitiesRepoImplDummy
import com.example.weather.domain.repo.city.CitiesRepoImplDummy.NoCityFoundException
import com.example.weather.domain.model.City
import com.example.weather.domain.model.Weather
import com.example.weather.domain.model.WeatherDTO

class WeathersRepoImplDummy : WeathersRepo {
    private val citiesRepo: CitiesRepo = CitiesRepoImplDummy()
    private val worldCitiesWeather: List<Weather> = setCitiesWeather(true)
    private val rusCitiesWeather: List<Weather> = setCitiesWeather(false)

    private fun setCitiesWeather(isWorld: Boolean): List<Weather> {
        val weathers: MutableList<Weather> = mutableListOf()
        val cities: List<City> = when (isWorld) {
            true -> {
                citiesRepo.getCitiesListWorld()
            }
            false -> {
                citiesRepo.getCitiesListRus()
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

    override fun getWeatherOfCity(listener: WeatherLoaderListener, lat: Double, lon: Double) {
        return try {
            Thread {
                SystemClock.sleep(1000)
                val city = citiesRepo.getCityByCoordinates(lat, lon)
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