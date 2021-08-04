package com.example.weather.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.weather.R

class ActionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append(context.getString(R.string.action_broadcast_receiver_msg_title))
            append(context.getString(R.string.action_broadcast_receiver_msg_action))
            append(intent.action)
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}