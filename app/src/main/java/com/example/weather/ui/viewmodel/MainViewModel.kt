package com.example.weather.ui.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Repo
import com.example.weather.model.RepoImpl

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repoImpl: Repo = RepoImpl()
) :
    ViewModel() {
    fun getLiveData() = liveDataToObserve

    fun getRusWeather() = getDataFromLocalSource(true)

    fun getWorldWeather() = getDataFromLocalSource(false)

    private fun getDataFromLocalSource(isRussianData: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(5000)
            liveDataToObserve.postValue(AppState.Success(
                if (isRussianData) repoImpl.getWeatherFromLocalStorageRus()
                else repoImpl.getWeatherFromLocalStorageWorld()))
        }.start()
    }
}