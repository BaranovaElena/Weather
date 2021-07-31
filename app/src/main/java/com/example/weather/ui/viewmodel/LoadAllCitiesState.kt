package com.example.weather.ui.viewmodel

import com.example.weather.model.Weather

sealed class LoadAllCitiesState {
    data class Success(val weatherDataRus: List<Weather>, val weatherDataWorld: List<Weather>) : LoadAllCitiesState()
    data class Error(val error: Throwable) : LoadAllCitiesState()
    object Loading : LoadAllCitiesState()
}
