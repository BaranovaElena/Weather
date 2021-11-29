package com.example.weather.ui.details

import com.example.weather.domain.model.City

sealed class LoadCityState {
    data class Success(val loadedCity: City) : LoadCityState()
    data class Error(val error: Throwable) : LoadCityState()
    object Loading : LoadCityState()
}
