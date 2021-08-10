package com.example.weather

import android.app.Application
import androidx.room.Room
import com.example.weather.domain.repo.weather.room.WeatherRoomDao
import com.example.weather.domain.repo.weather.room.WeatherRoomDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.weather.yandex.ru/"

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        private var appInstance: WeatherApplication? = null
        private var db: WeatherRoomDatabase? = null

        fun getWeatherRoomDao(): WeatherRoomDao {
            if (db == null) {
                synchronized(WeatherRoomDatabase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            WeatherRoomDatabase::class.java,
                            "WeatherHistory.db"
                        )
                            .build()
                    }
                }
            }
            return db!!.weatherRoomDao()
        }
    }
}