package com.example.weather.ui.history

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.WeatherApplication
import com.example.weather.domain.model.Weather
import com.example.weather.domain.repo.weather.room.HistoryLoaderListener
import com.example.weather.domain.repo.weather.room.WeatherHistoryRepo
import com.example.weather.domain.repo.weather.room.WeatherHistoryRepoRoomImpl

class HistoryViewModel : ViewModel() {
    val liveLoadStateValue: MutableLiveData<LoadHistoryState> = MutableLiveData()
    private val repo: WeatherHistoryRepo = WeatherHistoryRepoRoomImpl(
        WeatherApplication.getWeatherRoomDao(),
        Handler(Looper.getMainLooper())
    )

    private val onLoadListener: HistoryLoaderListener = object : HistoryLoaderListener {
        override fun onLoaded(weatherList: List<Weather>) {
            liveLoadStateValue.postValue(LoadHistoryState.Success(weatherList))
        }

        override fun onFailed(throwable: Throwable) {
            liveLoadStateValue.postValue(LoadHistoryState.Error(throwable))
        }
    }

    fun getWeather() = repo.getAllHistory(onLoadListener)
}