package com.example.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    var city: City = City(),
    var weatherDTO: WeatherDTO = WeatherDTO()
): Parcelable