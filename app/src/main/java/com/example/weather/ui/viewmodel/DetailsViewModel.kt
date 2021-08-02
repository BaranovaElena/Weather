package com.example.weather.ui.viewmodel

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.*

class DetailsViewModel : ViewModel() {
    private val liveLoadStateValue: MutableLiveData<LoadOneCityState> = MutableLiveData()
    private val weathersRepo: WeathersRepo = WeathersRepoImplDummy()
    private val citiesRepo: CitiesRepo = CitiesRepoImplDummy()

    fun getLiveAppStateValue() = liveLoadStateValue

    fun getWeather(lat: Double, lon: Double) =
        run {
            liveLoadStateValue.value = LoadOneCityState.Loading

            val onLoadListener: WeatherLoader.WeatherLoaderListener =
                object : WeatherLoader.WeatherLoaderListener {
                    override fun onLoaded(weatherDTO: WeatherDTO) {
                        liveLoadStateValue.postValue(
                            LoadOneCityState.Success(
                                weatherDTO
                            ))
                    }
                    override fun onFailed(throwable: Throwable) {
                        liveLoadStateValue.postValue(LoadOneCityState.Error(throwable))
                    }
                }
            val loader = WeatherLoader(onLoadListener, lat, lon)
            loader.loadWeather()
        }

    private fun getDataFromLocalSource() {
        Thread {
            SystemClock.sleep(1000)
            liveLoadStateValue.postValue(
                LoadOneCityState.Success(
                    weathersRepo.getWeatherOfDefaultCity().weatherDTO
                )
            )
        }.start()
    }

    fun getDefaultCity() = citiesRepo.getDefaultCity()
}