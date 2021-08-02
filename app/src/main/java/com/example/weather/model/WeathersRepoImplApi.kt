package com.example.weather.model

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.weather.keys.YANDEX_WEATHER_API_KEY
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeathersRepoImplApi: WeathersRepo {
    private val hostName = "https://api.weather.yandex.ru/v2/forecast?"

    override fun getWeatherOfRusCities(): List<Weather> {
        //реализация при необходимости выводить какие-то данные о погоде в общем списке городов
        return emptyList()
    }

    override fun getWeatherOfWorldCities(): List<Weather> {
        //реализация при необходимости выводить какие-то данные о погоде в общем списке городов
        return emptyList()
    }

    override fun getWeatherOfCity(listener: WeatherLoaderListener, lat: Double, lon: Double) {
        try {
            val uri = URL("${hostName}lat=${lat}&lon=${lon}")
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