package com.example.weather.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val name: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0
):Parcelable
