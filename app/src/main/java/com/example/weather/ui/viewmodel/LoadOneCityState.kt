package com.example.weather.ui.viewmodel

import com.example.weather.model.Weather

sealed class LoadOneCityState {
    data class Success(val weatherDataMos: Weather) : LoadOneCityState()
    data class Error(val error: Throwable) : LoadOneCityState()
    object Loading : LoadOneCityState()
}

