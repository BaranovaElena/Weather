package com.example.weather.notifications

import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.weather.R
import com.example.weather.notifications.Notifications.Companion.MORNING_CHECK_NOTIFICATION
import com.example.weather.notifications.Notifications.Companion.UNKNOWN_NOTIFICATION
import com.example.weather.notifications.Notifications.Companion.UPDATE_NOTIFICATION
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "@@@"
private const val CHANNEL_ID = "CHANNEL"
private const val CHANNEL_NAME = "firebase notification channel"

class NotificationService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channelDescription = getString(R.string.firebase_notif_channel_description)

        val notificationManager = NotificationManagerCompat.from(this)
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW
        )
            .setName(CHANNEL_NAME)
            .setDescription(channelDescription)
            .build()
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Message data payload: ${remoteMessage.notification}")

        when (remoteMessage.notification?.title) {
            MORNING_CHECK_NOTIFICATION -> {
                showNotification(MORNING_CHECK_NOTIFICATION, remoteMessage)
            }
            UPDATE_NOTIFICATION -> {
                showNotification(UPDATE_NOTIFICATION, remoteMessage)
            }
            else -> {
                showNotification(UNKNOWN_NOTIFICATION, remoteMessage)
            }
        }
    }

    private fun showNotification(notificationKey: String, remoteMessage: RemoteMessage) {
        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID).apply {
                setContentTitle(remoteMessage.notification?.title)
                setContentText(remoteMessage.notification?.body)
                setAutoCancel(false)
                when (notificationKey) {
                    MORNING_CHECK_NOTIFICATION -> {
                        setSmallIcon(R.drawable.ic_sun)
                        color = Color.YELLOW
                    }
                    UPDATE_NOTIFICATION -> {
                        setSmallIcon(R.drawable.ic_update)
                        color = Color.GREEN
                    }
                    UNKNOWN_NOTIFICATION -> {
                        setSmallIcon(R.drawable.ic_attention)
                        color = Color.RED
                    }
                }
            }.build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(Notifications.id[notificationKey]!!, notification)
    }
}