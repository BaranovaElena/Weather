package com.example.weather.domain.repo.city

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.example.weather.domain.model.City
import java.io.IOException

const val GPS_PERMISSION_REQUEST_CODE = 100
private const val REFRESH_PERIOD = 10000L
private const val MINIMAL_DISTANCE = 100f
const val GPS_PERMISSION_DENIED_EXCEPTION = "GPS_PERMISSION_DENIED"
const val GPS_TURNED_OFF_EXCEPTION = "GPS_TURN_OFF_EXCEPTION"
private const val UNKNOWN_CITY = "UNKNOWN CITY"

class CitiesRepoImplGps : CitiesRepo {
    var loadedCity: City = City()

    interface CityNameLoaderListener {
        fun onLoaded(city: City)
        fun onFailed(throwable: Throwable)
    }

    override fun getDefaultCity(context: Context, listener: CityLoaderListener) {
        val nameListener = createNameListener(listener)

        when (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            PackageManager.PERMISSION_DENIED -> {
                listener.onFailed(Throwable(GPS_PERMISSION_DENIED_EXCEPTION))
            }
            PackageManager.PERMISSION_GRANTED -> {
                loadLocation(context, nameListener, listener)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadLocation(
        context: Context,
        nameListener: CityNameLoaderListener,
        listener: CityLoaderListener
    ) {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val location: Location? =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        location?.let { loc ->
            setCity(context, loc, nameListener)

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                REFRESH_PERIOD, MINIMAL_DISTANCE
            ) {
                setCity(context, it, nameListener)
            }
        } ?: run {
            listener.onFailed(Throwable(GPS_TURNED_OFF_EXCEPTION))
        }
    }

    private fun setCity(
        context: Context,
        location: Location,
        nameListener: CityNameLoaderListener
    ) {
        loadedCity.lat = location.latitude
        loadedCity.lon = location.longitude
        getAddressAsync(context, location.latitude, location.longitude, nameListener)
    }

    private fun getAddressAsync(
        context: Context,
        lat: Double,
        lon: Double,
        listener: CityNameLoaderListener
    ) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(lat, lon, 1)
                val cityName =
                    "${addresses[0].locality ?: ""}  ${addresses[0].adminArea ?: ""}  ${addresses[0].countryName ?: ""}"

                listener.onLoaded(City(cityName))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun getCityByCoordinates(
        context: Context,
        lat: Double,
        lon: Double,
        listener: CityLoaderListener
    ) {
        val nameListener = createNameListener(listener)
        getAddressAsync(context, lat, lon, nameListener)
    }

    private fun createNameListener(listener: CityLoaderListener): CityNameLoaderListener {
        return object : CityNameLoaderListener {
            override fun onLoaded(city: City) {
                loadedCity.name = city.name
                listener.onLoaded(loadedCity)
            }

            override fun onFailed(throwable: Throwable) {
                loadedCity.name = UNKNOWN_CITY
                listener.onLoaded(loadedCity)
            }
        }
    }
}