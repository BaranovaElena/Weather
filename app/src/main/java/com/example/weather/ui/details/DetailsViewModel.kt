package com.example.weather.ui.details

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.WeatherApplication
import com.example.weather.domain.model.City
import com.example.weather.domain.model.Weather
import com.example.weather.domain.model.WeatherDTO
import com.example.weather.domain.model.setIconUri
import com.example.weather.domain.repo.city.*
import com.example.weather.domain.repo.weather.*
import com.example.weather.domain.repo.weather.room.WeatherHistoryRepo
import com.example.weather.domain.repo.weather.room.WeatherHistoryRepoRoomImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel : ViewModel() {
    val weatherLoadState: MutableLiveData<LoadOneWeatherState> = MutableLiveData()
    val cityLoadState: MutableLiveData<LoadCityState> = MutableLiveData()
    private val weathersRepo: WeathersRepo = WeathersRepoImplRetrofit(WeatherApplication.retrofit)
    private val citiesRepo: CitiesRepo = CitiesRepoImplGps()
    private val historyRepo: WeatherHistoryRepo = WeatherHistoryRepoRoomImpl(
        WeatherApplication.getWeatherRoomDao(),
        Handler(Looper.getMainLooper())
    )

    fun getWeather(city: City) =
        run {
            weatherLoadState.value = LoadOneWeatherState.Loading

            val onLoadListener: WeatherLoaderListener = object : WeatherLoaderListener {
                override fun onLoaded(weatherDTO: WeatherDTO) {
                    weatherDTO.setIconUri()
                    weatherLoadState.postValue(LoadOneWeatherState.Success(weatherDTO))
                    historyRepo.saveEntity(Weather(city, weatherDTO))
                }

                override fun onFailed(throwable: Throwable) {
                    weatherLoadState.postValue(LoadOneWeatherState.Error(throwable))
                }
            }

            val retrofitCallback = object : Callback<WeatherDTO> {
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    val serverResponse: WeatherDTO? = response.body()
                    weatherLoadState.postValue(
                        if (response.isSuccessful && serverResponse != null) {
                            serverResponse.setIconUri()
                            historyRepo.saveEntity(Weather(city, serverResponse))
                            LoadOneWeatherState.Success(serverResponse)
                        } else {
                            LoadOneWeatherState.Error(Throwable("SERVER_ERROR"))
                        }
                    )
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    weatherLoadState.postValue(LoadOneWeatherState.Error(t))
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

    fun getDefaultCity(context: Context) {
        cityLoadState.value = LoadCityState.Loading
        val listener: CityLoaderListener = object : CityLoaderListener {
            override fun onLoaded(city: City) {
                cityLoadState.postValue(LoadCityState.Success(city))
            }

            override fun onFailed(throwable: Throwable) {
                cityLoadState.postValue(LoadCityState.Error(throwable))
            }
        }
        citiesRepo.getDefaultCity(context, listener)
    }
}