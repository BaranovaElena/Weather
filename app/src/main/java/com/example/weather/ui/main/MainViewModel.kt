package com.example.weather.ui.main

import android.os.SystemClock.sleep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.AppState

class MainViewModel(private val liveDataToObserve: MutableLiveData<Any> = MutableLiveData()) :
    ViewModel() {
    fun getLiveData() {
        liveDataToObserve
    }

    fun getWeather() {
        getDataFromLocalSource()
    }

    fun getData(): LiveData<Any> {
        getDataFromLocalSource()
        return liveDataToObserve
    }

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(5000)
            liveDataToObserve.postValue(AppState.Success(Any()))
        }.start()
    }
}