package com.example.weather.ui.details

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.WeatherApplication
import com.example.weather.domain.model.City
import com.example.weather.domain.model.Weather
import com.example.weather.domain.model.WeatherDTO
import com.example.weather.domain.model.setIconUri
import com.example.weather.domain.repo.city.CitiesRepo
import com.example.weather.domain.repo.city.CitiesRepoImplDummy
import com.example.weather.domain.repo.weather.*
import com.example.weather.domain.repo.weather.room.WeatherHistoryRepo
import com.example.weather.domain.repo.weather.room.WeatherHistoryRepoRoomImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel : ViewModel() {
    val liveLoadStateValue: MutableLiveData<LoadOneCityState> = MutableLiveData()
    private val weathersRepo: WeathersRepo = WeathersRepoImplRetrofit(WeatherApplication.retrofit)
    private val citiesRepo: CitiesRepo = CitiesRepoImplDummy()
    private val historyRepo: WeatherHistoryRepo = WeatherHistoryRepoRoomImpl(
        WeatherApplication.getWeatherRoomDao(),
        Handler(Looper.getMainLooper())
    )

    fun getWeather(city: City) =
        run {
            liveLoadStateValue.value = LoadOneCityState.Loading

            val onLoadListener: WeatherLoaderListener = object : WeatherLoaderListener {
                override fun onLoaded(weatherDTO: WeatherDTO) {
                    weatherDTO.setIconUri()
                    liveLoadStateValue.postValue(LoadOneCityState.Success(weatherDTO))
                    historyRepo.saveEntity(Weather(city, weatherDTO))
                }

                override fun onFailed(throwable: Throwable) {
                    liveLoadStateValue.postValue(LoadOneCityState.Error(throwable))
                }
            }

            val retrofitCallback = object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    val serverResponse: WeatherDTO? = response.body()
                    liveLoadStateValue.postValue(
                        if (response.isSuccessful && serverResponse != null) {
                            serverResponse.setIconUri()
                            historyRepo.saveEntity(Weather(city, serverResponse))
                            LoadOneCityState.Success(serverResponse)
                        } else {
                            LoadOneCityState.Error(Throwable("SERVER_ERROR"))
                        }
                    )
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    liveLoadStateValue.postValue(LoadOneCityState.Error(t))
                }
            }

            when (weathersRepo) {
                is WeathersRepoImplRetrofit -> {
                    weathersRepo.getWeatherOfCity(city.lat, city.lon, retrofitCallback)
                }
                is WeathersRepoImplApi -> {
                    weathersRepo.getWeatherOfCity(city.lat, city.lon, onLoadListener)
                }
                is WeathersRepoImplDummy -> {
                    weathersRepo.getWeatherOfCity(city.lat, city.lon, onLoadListener)
                }
            }
        }

    fun getDefaultCity() = citiesRepo.getDefaultCity()
}