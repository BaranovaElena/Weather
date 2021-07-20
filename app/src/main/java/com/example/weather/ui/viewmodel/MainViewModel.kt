package com.example.weather.ui.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Repo
import com.example.weather.model.RepoImpl

class MainViewModel : ViewModel() {
    private val liveLoadStateValue: MutableLiveData<LoadState> = MutableLiveData()
    private val repo: Repo = RepoImpl()

    fun getLiveAppStateValue() = liveLoadStateValue

    fun getRusWeather() = getDataFromLocalSource(true)

    fun getWorldWeather() = getDataFromLocalSource(false)

    private fun getDataFromLocalSource(isRussianData: Boolean) {
        liveLoadStateValue.value = LoadState.Loading
        Thread {
            sleep(5000)
            liveLoadStateValue.postValue(LoadState.Success(
                if (isRussianData) repo.getWeatherFromLocalStorageRus()
                else repo.getWeatherFromLocalStorageWorld()))
        }.start()
    }
}