package com.example.weather.ui.citylist

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.domain.repo.city.lists.CityListsRepo
import com.example.weather.domain.repo.city.lists.CityListsRepoRus

class CityListViewModel : ViewModel() {
    val loadStateLiveData: MutableLiveData<LoadAllCitiesState> = MutableLiveData()
    private val cityListsRepo: CityListsRepo = CityListsRepoRus()

    fun getCityLists() {
        loadStateLiveData.value = LoadAllCitiesState.Loading
        Thread {
            sleep(1000)
            loadStateLiveData.postValue(
                LoadAllCitiesState.Success(
                    cityListsRepo.getLocalCitiesList(),
                    cityListsRepo.getWorldCitiesList()
                )
            )
        }.start()
    }
}