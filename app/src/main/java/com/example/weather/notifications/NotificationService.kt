package com.example.weather.notifications

import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.weather.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val TAG = "@@@"
private const val CHANNEL_ID = "CHANNEL"

private const val MORNING_CHECK_NOTIFICATION = "Morning check weather"
private const val UPDATE_NOTIFICATION = "Update app"
private const val UNKNOWN_NOTIFICATION = "Unknown notification"

class NotificationService : FirebaseMessagingService() {
    companion object {
        private val notificationsMap: Map<String, Int> = mapOf(
            Pair(MORNING_CHECK_NOTIFICATION, 11),
            Pair(UPDATE_NOTIFICATION, 12),
            Pair(UNKNOWN_NOTIFICATION, 13)
        )
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channelName = "channel name"
        val channelDescription = "check work of notification channel"

        val notificationManager = NotificationManagerCompat.from(this)
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW
        )
            .setName(channelName)
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
        notificationManager.notify(notificationsMap[notificationKey]!!, notification)
    }
}