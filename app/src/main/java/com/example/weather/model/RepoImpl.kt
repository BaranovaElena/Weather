package com.example.weather.model

class RepoImpl : Repo {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }
}