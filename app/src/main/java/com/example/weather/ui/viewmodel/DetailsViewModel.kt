package com.example.weather.ui.viewmodel

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Repo
import com.example.weather.model.RepoImpl

class DetailsViewModel(
    private val liveDataToObserve: MutableLiveData<AppStateLoadOne> = MutableLiveData(),
    private val repoImpl: Repo = RepoImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeather() {
        liveDataToObserve.value = AppStateLoadOne.Loading
        Thread {
            SystemClock.sleep(1000)
            liveDataToObserve.postValue(
                AppStateLoadOne.Success(
                    repoImpl.getWeatherFromLocalStorageRus()[0]
                )
            )
        }.start()
    }
}