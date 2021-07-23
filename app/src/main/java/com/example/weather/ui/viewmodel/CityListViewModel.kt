package com.example.weather.ui.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Repo
import com.example.weather.model.RepoImpl

class MainViewModel : ViewModel() {
    private val liveLoadStateValue: MutableLiveData<AppStateLoadAll> = MutableLiveData()
    private val repo: Repo = RepoImpl()

    fun getLiveAppStateValue() = liveLoadStateValue

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveLoadStateValue.value = AppStateLoadAll.Loading
        Thread {
            sleep(1000)
            liveLoadStateValue.postValue(
                AppStateLoadAll.Success(
                    repo.getWeatherFromLocalStorageRus(),
                    repo.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }
}