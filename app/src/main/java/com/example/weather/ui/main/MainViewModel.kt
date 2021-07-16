package com.example.weather.ui.main

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.AppState

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) :
    ViewModel() {
    fun getLiveData() = liveDataToObserve

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(5000)
            liveDataToObserve.postValue(AppState.Success(Any()))
        }.start()
    }
}