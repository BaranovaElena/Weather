package com.example.weather.ui.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.WeathersRepo
import com.example.weather.model.WeathersRepoImplDummy

class CityListViewModel : ViewModel() {
    private val liveLoadStateValue: MutableLiveData<LoadAllCitiesState> = MutableLiveData()
    private val repo: WeathersRepo = WeathersRepoImplDummy()
    fun getLiveAppStateValue() = liveLoadStateValue

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveLoadStateValue.value = LoadAllCitiesState.Loading
        Thread {
            sleep(1000)
            liveLoadStateValue.postValue(
                LoadAllCitiesState.Success(
                    repo.getWeatherOfRusCities(),
                    repo.getWeatherOfWorldCities()
                )
            )
        }.start()
    }
}