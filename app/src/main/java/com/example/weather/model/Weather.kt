package com.example.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = City(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val condition: String = "",
    val windSpeed: Int = 0,
    val windDir: String = "",
    val pressureMm : Int = 0,
    val humidity: Int = 0
): Parcelable