package com.example.weather.ui.details

import com.example.weather.domain.model.WeatherDTO

sealed class LoadOneCityState {
    data class Success(val loadedWeather: WeatherDTO) : LoadOneCityState()
    data class Error(val error: Throwable) : LoadOneCityState()
    object Loading : LoadOneCityState()
}

