package com.example.weather.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val liveDataToObserve: MutableLiveData<Any> = MutableLiveData()) :
    ViewModel() {

    fun getData(): LiveData<Any> {
        getDataFromLocalSource()
        return liveDataToObserve
    }

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = "SEND DATA"
    }
}