package com.example.weather.ui.viewmodel

import com.example.weather.model.Weather

sealed class AppState {
    data class Success(val weatherDataRus: List<Weather>, val weatherDataWorld: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
