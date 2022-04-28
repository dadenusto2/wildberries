package com.example.week1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import java.util.*


class BluetoothReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pm : PackageManager = context.packageManager
        val action = intent.action
        if (action == Intent.ACTION_TIMEZONE_CHANGED) {
            Toast.makeText(context,  pm.getApplicationInfo( context.getPackageName(), 0).toString() + ": Изменен часовой пояс", Toast.LENGTH_SHORT).show()
        }
    }
}