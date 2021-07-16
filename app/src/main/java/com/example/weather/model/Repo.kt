package com.example.weather.model

interface Repo {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}