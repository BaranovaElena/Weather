package com.example.weather.ui.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Repo
import com.example.weather.model.RepoImpl

class CityListViewModel : ViewModel() {
    private val liveLoadStateValue: MutableLiveData<LoadAllCitiesState> = MutableLiveData()
    private val repo: Repo = RepoImpl()
    fun getLiveAppStateValue() = liveLoadStateValue

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveLoadStateValue.value = LoadAllCitiesState.Loading
        Thread {
            sleep(1000)
            liveLoadStateValue.postValue(
                LoadAllCitiesState.Success(
                    repo.getWeatherFromLocalStorageRus(),
                    repo.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }
}