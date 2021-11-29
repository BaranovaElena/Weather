package com.example.weather.ui.history

import com.example.weather.domain.model.Weather

sealed class LoadHistoryState {
    data class Success(val weatherData: List<Weather>) : LoadHistoryState()
    data class Error(val error: Throwable) : LoadHistoryState()
    object Loading : LoadHistoryState()
}
