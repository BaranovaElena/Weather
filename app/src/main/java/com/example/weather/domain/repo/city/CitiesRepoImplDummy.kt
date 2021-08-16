package com.example.weather.domain.repo.city

import android.content.Context
import com.example.weather.domain.model.City
import com.example.weather.domain.repo.city.lists.CityListsRepoRus
import kotlin.math.abs

class CitiesRepoImplDummy : CitiesRepo {
    private val worldList = CityListsRepoRus().getWorldCitiesList()
    private val localList = CityListsRepoRus().getLocalCitiesList()

    override fun getCityByCoordinates(lat: Double, lon: Double): City {
        val eps = 0.000001
        for (city in localList) {
            if ((abs(city.lat - lat) <= eps) && (abs(city.lon - lon) <= eps)) {
                return city
            }
        }
        for (city in worldList) {
            if ((abs(city.lat - lat) <= eps) && (abs(city.lon - lon) <= eps)) {
                return city
            }
        }
        throw NoCityFoundException()
    }

    override fun getDefaultCity(context: Context, listener: CityLoaderListener){
        listener.onLoaded(localList[0])
    }

    class NoCityFoundException : Exception() {
        override val message = "This city is not founded in dummy storage"
    }
}