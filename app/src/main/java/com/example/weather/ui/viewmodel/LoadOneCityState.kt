package com.example.weather.ui.viewmodel

import com.example.weather.model.WeatherDTO

sealed class LoadOneCityState {
    data class Success(val loadedWeather: WeatherDTO) : LoadOneCityState()
    data class Error(val error: Throwable) : LoadOneCityState()
    object Loading : LoadOneCityState()
}

