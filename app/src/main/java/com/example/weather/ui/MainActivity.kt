package com.example.weather.ui

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weather.R
import com.example.weather.databinding.MainActivityBinding
import com.example.weather.domain.model.City
import com.example.weather.notifications.*
import com.example.weather.ui.citylist.CityListFragment
import com.example.weather.ui.details.DetailsFragment
import com.example.weather.ui.favorites.FavoritesFragment
import com.example.weather.ui.history.HistoryFragment
import com.example.weather.ui.map.MapsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val CHANNEL_ID = "app notification channel"
private const val CHANNEL_NAME = "app notification channel"
private const val NOTIFICATION_REQUEST_CODE = 0

class MainActivity : AppCompatActivity(), CityListFragment.Controller {
    private lateinit var binding: MainActivityBinding
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.navView
        bottomNavigationView?.setOnItemSelectedListener { item -> setBottomNavListener(item) }

        createNotificationChannel()
    }

    private fun setBottomNavListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_list -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CityListFragment.newInstance())
                    .commitNow()
            }
            R.id.navigation_default -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DetailsFragment.newInstance(null))
                    .commitNow()
            }
            R.id.navigation_favorites -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FavoritesFragment.newInstance())
                    .commitNow()
            }
            R.id.navigation_history -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HistoryFragment.newInstance())
                    .commitNow()
            }
        }
        return true
    }

    override fun openDetailsScreen(city: City) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, DetailsFragment.newInstance(city))
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun openMap() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, MapsFragment.newInstance())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_bar_menu_notify -> {
                createNotification()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNotificationChannel() {
        val channelDescription = getString(R.string.menu_notif_channel_description)

        val notificationManager = NotificationManagerCompat.from(this)
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW
        )
            .setName(CHANNEL_NAME)
            .setDescription(channelDescription)
            .build()
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val remoteView = RemoteViews(packageName, R.layout.fragment_notification)
        remoteView.setOnClickPendingIntent(R.id.notification_button, pendingIntent)

        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID).apply {
                setAutoCancel(true)
                setSmallIcon(R.drawable.ic_notification)
                color = Color.RED
                priority = NotificationCompat.PRIORITY_HIGH
                setContent(remoteView)
            }.build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(
            Notifications.id[Notifications.APP_NOTIFICATION]!!, notification
        )
    }
}