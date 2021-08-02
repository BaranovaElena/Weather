package com.example.weather.model

import android.util.Log
import com.example.weather.model.CitiesRepoImplDummy.NoCityFoundException

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
        var temp = 1
        var feelsLike = 2

        for (city in cities) {
            weathers.add(Weather(city, temp, feelsLike))
            temp += 2
            feelsLike += 2
        }
        return weathers
    }

    override fun getWeatherOfRusCities() = rusCitiesWeather

    override fun getWeatherOfWorldCities() = worldCitiesWeather

    override fun getWeatherOfCity(lat: Double, lon: Double): Weather {
        return try {
            val city = citiesRepo.getCityByCoordinates(lat, lon)
            getWeatherOfCity(city)
        } catch (e: NoCityFoundException) {
            Log.d("@@@", e.message)
            Weather()
        }
    }

    private fun getWeatherOfCity(city: City): Weather {
        for (weather in rusCitiesWeather) {
            if (weather.city == city) {
                return weather
            }
        }
        for (weather in worldCitiesWeather) {
            if (weather.city == city) {
                return weather
            }
        }
        throw NoCityFoundException()
    }

    override fun getWeatherOfDefaultCity(): Weather {
        return Weather(
            citiesRepo.getDefaultCity(),
            1, 2, "clear", 10, "nw", 750, 40
        )
    }
}