package com.example.weather.domain.repo.city.lists

import com.example.weather.domain.model.City

interface CityListsRepo {
    fun getLocalCitiesList(): List<City>
    fun getWorldCitiesList(): List<City>
}