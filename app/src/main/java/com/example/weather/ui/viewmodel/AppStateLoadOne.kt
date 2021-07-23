package com.example.weather.ui.viewmodel

import com.example.weather.model.Weather

sealed class AppStateLoadOne {
    data class Success(val weatherDataMos: Weather) : AppStateLoadOne()
    data class Error(val error: Throwable) : AppStateLoadOne()
    object Loading : AppStateLoadOne()
}

