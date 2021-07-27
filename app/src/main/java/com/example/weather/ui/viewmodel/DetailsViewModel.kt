package com.example.weather.ui.viewmodel

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Repo
import com.example.weather.model.RepoImpl

class DetailsViewModel : ViewModel() {
    private val liveLoadStateValue: MutableLiveData<LoadOneCityState> = MutableLiveData()
    private val repo: Repo = RepoImpl()

    fun getLiveAppStateValue() = liveLoadStateValue

    fun getWeather() {
        liveLoadStateValue.value = LoadOneCityState.Loading
        Thread {
            SystemClock.sleep(1000)
            liveLoadStateValue.postValue(
                LoadOneCityState.Success(
                    repo.getWeatherFromLocalStorageRus()[0]
                )
            )
        }.start()
    }
    fun getDefaultCity() = repo.getDefaultCity()
}