package com.example.weather.notifications

class Notifications {
    companion object {
        const val MORNING_CHECK_NOTIFICATION = "Morning check weather"
        const val UPDATE_NOTIFICATION = "Update app"
        const val UNKNOWN_NOTIFICATION = "Unknown notification"
        const val APP_NOTIFICATION = "App notification"

        val id: Map<String, Int> = mapOf(
            Pair(MORNING_CHECK_NOTIFICATION, 11),
            Pair(UPDATE_NOTIFICATION, 12),
            Pair(UNKNOWN_NOTIFICATION, 13),
            Pair(APP_NOTIFICATION, 14)
        )
    }
}
