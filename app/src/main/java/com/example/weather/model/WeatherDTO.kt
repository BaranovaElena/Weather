package com.example.weather.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO(
    val fact: FactDTO? = FactDTO()
) : Parcelable

@Parcelize
data class FactDTO(
    @SerializedName("temp")
    val temperature: Int? = 0,
    @SerializedName("feels_like")
    val feelsLike: Int? = 0,
    val condition: String? = "",
    @SerializedName("wind_speed")
    val windSpeed: Double? = 0.0,
    @SerializedName("wind_dir")
    val windDir: String? = "",
    @SerializedName("pressure_mm")
    val pressureMm: Int? = 0,
    val humidity: Int? = 0
) : Parcelable

