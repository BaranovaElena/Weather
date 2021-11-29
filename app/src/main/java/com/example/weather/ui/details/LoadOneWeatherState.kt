package com.example.weather.ui.details

import com.example.weather.domain.model.WeatherDTO

sealed class LoadOneWeatherState {
    data class Success(val loadedWeather: WeatherDTO) : LoadOneWeatherState()
    data class Error(val error: Throwable) : LoadOneWeatherState()
    object Loading : LoadOneWeatherState()
}

