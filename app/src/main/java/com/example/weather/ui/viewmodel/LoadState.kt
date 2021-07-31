package com.example.weather.ui.viewmodel

import com.example.weather.model.Weather

sealed class LoadState {
    data class Success(val weatherData: Weather) : LoadState()
    data class Error(val error: Throwable) : LoadState()
    object Loading : LoadState()
}
