package com.example.weather.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.WeatherApplication
import com.example.weather.domain.model.WeatherDTO
import com.example.weather.domain.repo.city.CitiesRepo
import com.example.weather.domain.repo.city.CitiesRepoImplDummy
import com.example.weather.domain.repo.weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel : ViewModel() {
    val liveLoadStateValue: MutableLiveData<LoadOneCityState> = MutableLiveData()
    private val weathersRepo: WeathersRepo = WeathersRepoImplRetrofit(WeatherApplication.retrofit)
    private val citiesRepo: CitiesRepo = CitiesRepoImplDummy()

    fun getWeather(lat: Double, lon: Double) =
        run {
            liveLoadStateValue.value = LoadOneCityState.Loading

            val onLoadListener: WeatherLoaderListener = object : WeatherLoaderListener {
                override fun onLoaded(weatherDTO: WeatherDTO) {
                    liveLoadStateValue.postValue(LoadOneCityState.Success(weatherDTO))
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
                    weathersRepo.getWeatherOfCity(lat, lon, retrofitCallback)
                }
                is WeathersRepoImplApi -> {
                    weathersRepo.getWeatherOfCity(lat, lon, onLoadListener)
                }
                is WeathersRepoImplDummy -> {
                    weathersRepo.getWeatherOfCity(lat, lon, onLoadListener)
                }
            }
        }

    fun getDefaultCity() = citiesRepo.getDefaultCity()
}