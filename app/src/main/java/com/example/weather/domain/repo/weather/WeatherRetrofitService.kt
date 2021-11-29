package com.example.weather.domain.repo.weather

import com.example.weather.BuildConfig
import com.example.weather.domain.model.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherRetrofitService {
    @GET(BuildConfig.YANDEX_WEATHER_TARIFF_URL)
    fun getWeather(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}