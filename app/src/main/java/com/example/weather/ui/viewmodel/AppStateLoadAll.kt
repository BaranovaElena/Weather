package com.example.weather.ui.viewmodel

import com.example.weather.model.Weather

sealed class AppStateLoadAll {
    data class Success(val weatherDataRus: List<Weather>, val weatherDataWorld: List<Weather>) : AppStateLoadAll()
    data class Error(val error: Throwable) : AppStateLoadAll()
    object Loading : AppStateLoadAll()
}
