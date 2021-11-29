package com.example.weather.ui.map

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.domain.model.City
import com.example.weather.domain.repo.city.CitiesRepo
import com.example.weather.domain.repo.city.CitiesRepoImplGps
import com.example.weather.domain.repo.city.CityLoaderListener
import com.example.weather.ui.details.LoadCityState
import com.google.android.gms.maps.model.LatLng

class MapsViewModel: ViewModel() {
    val cityLoadState: MutableLiveData<LoadCityState> = MutableLiveData()
    private val citiesRepo: CitiesRepo = CitiesRepoImplGps()

    fun getAddress(context: Context, latLng: LatLng) {
        cityLoadState.value = LoadCityState.Loading

        val listener: CityLoaderListener = object : CityLoaderListener {
            override fun onLoaded(city: City) {
                cityLoadState.postValue(LoadCityState.Success(city))
            }

            override fun onFailed(throwable: Throwable) {
                cityLoadState.postValue(LoadCityState.Error(throwable))
            }
        }
        citiesRepo.getCityByCoordinates(context, latLng.latitude, latLng.longitude, listener)
    }
}