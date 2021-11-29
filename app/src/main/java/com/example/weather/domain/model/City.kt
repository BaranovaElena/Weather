package com.example.weather.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    var name: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    val image: String = ""
):Parcelable
