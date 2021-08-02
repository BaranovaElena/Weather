package com.example.weather.model

class WeatherRepoImplApi: WeathersRepo {
    private val citiesRepo: CitiesRepo = CitiesRepoImplDummy()

    override fun getWeatherOfRusCities(): List<Weather> {
        TODO("Not yet implemented")
    }

    override fun getWeatherOfWorldCities(): List<Weather> {
        TODO("Not yet implemented")
    }

    override fun getWeatherOfCity(lat: Double, lon: Double): Weather {
        TODO("Not yet implemented")
    }

    override fun getWeatherOfDefaultCity(): Weather {
        TODO("Not yet implemented")
    }
}