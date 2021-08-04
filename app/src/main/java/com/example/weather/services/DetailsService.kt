package com.example.weather.services

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.keys.YANDEX_WEATHER_API_KEY
import com.example.weather.model.WeatherDTO
import com.example.weather.view.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
private const val REQUEST_API_KEY = "X-Yandex-API-Key"
private const val HOST_NAME = "https://api.weather.yandex.ru/v2/forecast?"

class DetailsService(name: String = "DetailService") : IntentService(name) {
    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = intent.getDoubleExtra(LATITUDE_EXTRA, 0.0)
            val lon = intent.getDoubleExtra(LONGITUDE_EXTRA, 0.0)
            if (lat == 0.0 && lon == 0.0) {
                sendDetailsBroadcast(DETAILS_DATA_EMPTY_EXTRA)
            } else {
                loadWeather(lat.toString(), lon.toString())
            }
        } ?: sendDetailsBroadcast(DETAILS_INTENT_EMPTY_EXTRA)
    }

    private fun loadWeather(lat: String, lon: String) {
        try {
            val uri = URL("${HOST_NAME}lat=${lat}&lon=${lon}")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                getWeatherWithURL(urlConnection)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            sendDetailsBroadcast(DETAILS_URL_MALFORMED_EXTRA)
        }
    }

    private fun getWeatherWithURL(urlConnection: HttpsURLConnection) {
        urlConnection.apply {
            requestMethod = REQUEST_GET
            readTimeout = REQUEST_TIMEOUT
            addRequestProperty(REQUEST_API_KEY, YANDEX_WEATHER_API_KEY)
        }
        val weatherDTO: WeatherDTO = Gson().fromJson(
            BufferedReader(InputStreamReader(urlConnection.inputStream))
                .readLines().joinToString("\n"),
            WeatherDTO::class.java
        )
        onSuccessResponse(weatherDTO)
    }

    private fun onSuccessResponse(weatherDTO: WeatherDTO) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_WEATHER_DTO_EXTRA, weatherDTO)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun sendDetailsBroadcast(details: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, details)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }
}