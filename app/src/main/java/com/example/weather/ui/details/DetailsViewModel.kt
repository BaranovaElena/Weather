package com.example.weather.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.domain.*
import com.example.weather.domain.model.WeatherDTO
import com.example.weather.domain.repo.city.CitiesRepo
import com.example.weather.domain.repo.city.CitiesRepoImplDummy
import com.example.weather.domain.repo.weather.WeatherLoaderListener
import com.example.weather.domain.repo.weather.WeathersRepo
import com.example.weather.domain.repo.weather.WeathersRepoImplApi

class DetailsViewModel : ViewModel() {
    private val liveLoadStateValue: MutableLiveData<LoadOneCityState> = MutableLiveData()
    private val weathersRepo: WeathersRepo = WeathersRepoImplApi()
    private val citiesRepo: CitiesRepo = CitiesRepoImplDummy()

    fun getLiveAppStateValue() = liveLoadStateValue

    fun getWeather(lat: Double, lon: Double) =
        run {
            liveLoadStateValue.value = LoadOneCityState.Loading

            val onLoadListener: WeatherLoaderListener =
                object : WeatherLoaderListener {
                    override fun onLoaded(weatherDTO: WeatherDTO) {
                        liveLoadStateValue.postValue(LoadOneCityState.Success(weatherDTO))
                    }
                    override fun onFailed(throwable: Throwable) {
                        liveLoadStateValue.postValue(LoadOneCityState.Error(throwable))
                    }
                }
            weathersRepo.getWeatherOfCity(onLoadListener, lat, lon)
        }

    fun getDefaultCity() = citiesRepo.getDefaultCity()
}