package com.example.weather.ui.citylist

import com.example.weather.domain.model.City
import com.example.weather.domain.model.Weather

sealed class LoadAllCitiesState {
    data class Success(val localCities: List<City>, val worldCities: List<City>) : LoadAllCitiesState()
    data class Error(val error: Throwable) : LoadAllCitiesState()
    object Loading : LoadAllCitiesState()
}
