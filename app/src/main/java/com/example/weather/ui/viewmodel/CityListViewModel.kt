package com.example.weather.ui.viewmodel

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Repo
import com.example.weather.model.RepoImpl

class CityListViewModel(
    private val liveDataToObserve: MutableLiveData<AppStateLoadAll> = MutableLiveData(),
    private val repoImpl: Repo = RepoImpl()
) : ViewModel() {
    fun getLiveData() = liveDataToObserve

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppStateLoadAll.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(
                AppStateLoadAll.Success(
                    repoImpl.getWeatherFromLocalStorageRus(),
                    repoImpl.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }
}