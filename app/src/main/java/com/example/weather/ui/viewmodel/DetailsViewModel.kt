package com.example.weather.ui.viewmodel

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.WeathersRepo
import com.example.weather.model.WeathersRepoImplDummy

class DetailsViewModel : ViewModel() {
    private val liveLoadStateValue: MutableLiveData<LoadOneCityState> = MutableLiveData()
    private val repo: WeathersRepo = WeathersRepoImplDummy()

    fun getLiveAppStateValue() = liveLoadStateValue

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource(){
        liveLoadStateValue.value = LoadOneCityState.Loading
        Thread {
            SystemClock.sleep(1000)
            liveLoadStateValue.postValue(
                LoadOneCityState.Success(
                    repo.getWeatherOfRusCities()[0]
                )
            )
        }.start()
    }

    fun getDefaultCityWeather() = repo.getWeatherOfDefaultCity()
}