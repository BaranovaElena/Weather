package com.example.weather.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.weather.keys.YANDEX_WEATHER_API_KEY
import com.example.weather.model.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val listener: WeatherLoaderListener,
                    private val lat: Double,
                    private val lon: Double) {
    interface WeatherLoaderListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }

    fun loadWeather() {
        try {
            val uri = URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}")
            val handler = Handler(Looper.getMainLooper())
            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty("X-Yandex-API-Key", YANDEX_WEATHER_API_KEY)
                    urlConnection.readTimeout = 10000
                    val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO: WeatherDTO = Gson().fromJson(getLines(bufferedReader), WeatherDTO::class.java)
                    handler.post { listener.onLoaded(weatherDTO) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }
    }

    private fun getLines(reader: BufferedReader): String {
        return reader.readLines().joinToString("\n")
    }
}