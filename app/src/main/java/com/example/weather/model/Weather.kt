package com.example.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = City(
        "Москва",
        55.755826,
        37.617299900000035
    ),
    val temperature: Int = 0,
    val feelsLike: Int = 0
): Parcelable